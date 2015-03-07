package br.ufrj.cos.prisma.miner.openxes;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import minerv1.Commit;
import minerv1.Event;
import minerv1.FrameworkApplication;
import minerv1.FrameworkProcess;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XIdentityExtension;
import org.deckfour.xes.extension.std.XLifecycleExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactoryBufferedImpl;
import org.deckfour.xes.id.XID;
import org.deckfour.xes.id.XIDFactory;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.out.XesXmlSerializer;
import org.deckfour.xes.xstream.XesXStreamPersistency;

import br.ufrj.cos.prisma.helpers.LogHelper;
import br.ufrj.cos.prisma.miner.util.Constants;
import br.ufrj.cos.prisma.miner.util.TopologicalSort;

import com.thoughtworks.xstream.XStream;

public class XESLogGenerator {

	private XFactoryBufferedImpl factory;
	private XLog log;
	private boolean classesOnly = false;
	private String exportPath;

	public XESLogGenerator(String exportPath) {
		init();
		this.exportPath = exportPath;
	}

	public XESLogGenerator(boolean classesOnly, String exportPath) {
		init();
		this.classesOnly = classesOnly;
		this.exportPath = exportPath;
	}

	private void init() {
		factory = new XFactoryBufferedImpl();

		try {
			XAttributeMap attributes = factory.createAttributeMap();
			XAttribute nameAttr = factory.createAttributeLiteral(
					XConceptExtension.KEY_NAME, "Framework process",
					XConceptExtension.instance());
			attributes.put(XConceptExtension.KEY_NAME, nameAttr);

			log = factory.createLog(attributes);

		} catch (Exception e) {
			log = null;
			e.printStackTrace();
		}
	}

	public XTrace createNewTrace(String frameworkInstance) {
		XAttributeMap attributes = factory.createAttributeMap();
		XID traceId = XIDFactory.instance().createId();
		XAttribute idAttr = factory.createAttributeID(
				XIdentityExtension.KEY_ID, traceId,
				XIdentityExtension.instance());
		XAttribute nameAttr = factory.createAttributeLiteral(
				XConceptExtension.KEY_NAME, frameworkInstance,
				XConceptExtension.instance());
		attributes.put(XIdentityExtension.KEY_ID, idAttr);
		attributes.put(XConceptExtension.KEY_NAME, nameAttr);

		XTrace trace = factory.createTrace(attributes);
		return trace;
	}

	public XEvent createEvent(Event appEvent) {
		if (appEvent.getActivity() == null) {
			return null;
		}

		String type = appEvent.getActivity().getType().getName();
		String packageName = appEvent.getActivity().getPackageName();
		
		String eventName = String.format("%s", appEvent.getActivity().getName());
		if (packageName != null && packageName.length() > 0) {
			eventName = String.format("%s.%s", appEvent.getActivity()
					.getPackageName(), appEvent.getActivity().getName());
		}
				
		// DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return createEvent(type, eventName, cal.getTime());
	}

	public XEvent createEvent(String type, String name, Date timestamp) {
		XEvent event = null;

		type = (type.toLowerCase().equals("class_extension")) ? Constants.CLASS_EXTENSION
				: Constants.METHOD_EXTENSION;
		if (classesOnly && type.equals(Constants.METHOD_EXTENSION)) {
			return null;
		}

		String activityName = String.format("%s_%s", type, name);
		try {
			XAttributeMap attributes = factory.createAttributeMap();
			XID eventId = XIDFactory.instance().createId();
			XAttribute idAttr = factory.createAttributeID(
					XIdentityExtension.KEY_ID, eventId,
					XIdentityExtension.instance());
			XAttribute nameAttr = factory.createAttributeLiteral(
					XConceptExtension.KEY_NAME, activityName,
					XConceptExtension.instance());

			XAttribute lifecycleAttr = factory.createAttributeLiteral(
					XLifecycleExtension.KEY_TRANSITION, "complete",
					XLifecycleExtension.instance());

			if (timestamp != null) {
				XAttribute timestampAttr = factory.createAttributeTimestamp(
						XTimeExtension.KEY_TIMESTAMP, timestamp,
						XTimeExtension.instance());
				attributes.put(XTimeExtension.KEY_TIMESTAMP, timestampAttr);
			}

			attributes.put(XIdentityExtension.KEY_ID, idAttr);
			attributes.put(XConceptExtension.KEY_NAME, nameAttr);
			attributes.put(XLifecycleExtension.KEY_TRANSITION, lifecycleAttr);

			event = factory.createEvent(attributes);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return event;
	}

	public void getXESRepresentationForCommits(FrameworkProcess fwProcess) {
		XTrace trace = null;
		XEvent event = null;

		for (FrameworkApplication application : fwProcess.getApplications()) {
			if (!validApp(application)) {
				continue;
			}

			for (Commit c : application.getCommits()) {
				System.out.println("commit: " + c.getId());
				trace = createNewTrace(c.getName());

				if (trace == null) {
					System.out.println("Error creating trace for commit "
							+ c.getName());
					continue;
				}

				int addedEvents = 0;
				TopologicalSort tp = new TopologicalSort(c);
				for (Event e : tp.getSortedEventsForCommit()) {
					event = createEvent(e);
					if (event != null) {
						trace.add(event);
						addedEvents++;
					}
				}

				// Prevent adding empty traces
				if (addedEvents > 0) {
					log.add(trace);
				}
			}

		}
	}

	public void getXESRepresentationFromProcess(FrameworkProcess fwProcess) {
		XTrace trace = null;
		XEvent event = null;

		for (FrameworkApplication application : fwProcess.getApplications()) {
			if (application == null) {
				System.out.println("Null application");
				continue;
			}

			String appName = application.getName();
			if (application.getEventsCount() == 0) {
				System.out.println("Empty trace for application: " + appName);
				continue;
			}

			trace = createNewTrace(appName);
			if (trace == null) {
				System.out.println("Error creating trace for application "
						+ appName);
				continue;
			}

			int addedEvents = 0;

			TopologicalSort tp = new TopologicalSort(application);
			for (Event e : tp.getSortedEvents()) {
				event = createEvent(e);
				if (event != null) {
					trace.add(event);
					addedEvents++;
				}
			}

			// Prevent adding empty traces
			if (addedEvents > 0) {
				log.add(trace);
			}
		}
	}

	public boolean validApp(FrameworkApplication application) {
		if (application == null) {
			System.out.println("Null application");
			return false;
		}

		String appName = application.getName();
		if (application.getEventsCount() == 0) {
			System.out.println("Empty trace for application: " + appName);
			return false;
		}

		System.out.println("Application: " + appName + " is valid.");
		return true;
	}

	public void serialize(String filename) {
		XStream xstream = new XStream();
		XesXStreamPersistency.register(xstream);
		XesXmlSerializer serializer = new XesXmlSerializer();

		try {
			File dir = new File(this.exportPath);
			if (!dir.exists()) {
				dir.mkdir();
			}

			File sFile = new File(dir, filename);

			LogHelper.log("Serializing log with XStream at: "
					+ sFile.getAbsolutePath().toString());

			OutputStream oStream = new BufferedOutputStream(
					new FileOutputStream(sFile));
			// xstream.toXML(log, oStream);
			serializer.serialize(log, oStream);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public XLog getLog() {
		return log;
	}
}