package br.ufrj.cos.prisma.miner.openxes;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import br.ufrj.cos.prisma.model.ClassProcess;
import br.ufrj.cos.prisma.model.ClassProcess.ClassProcessOccurrence;
import br.ufrj.cos.prisma.model.ClassToProcessMap;
import br.ufrj.cos.prisma.model.ReuseMinerApplicationTree;
import br.ufrj.cos.prisma.model.ReuseMinerApplicationTree.CustomNode;
import br.ufrj.cos.prisma.model.ReuseMinerApplicationTree.VisitorStrategy;

import com.thoughtworks.xstream.XStream;

public class XESLogGenerator {

	private XFactoryBufferedImpl factory;
	private XLog log;
	private boolean classesOnly = false;
	private String exportPath;

	public XESLogGenerator(String exportPath) {
		this.factory = new XFactoryBufferedImpl();
		this.exportPath = exportPath;
	}

	public XESLogGenerator(boolean classesOnly, String exportPath) {
		this.factory = new XFactoryBufferedImpl();
		this.classesOnly = classesOnly;
		this.exportPath = exportPath;
	}

	private XLog initEventLog() {
		XLog log = null;
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
		return log;
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

	public XEvent createEvent(String type, String eventId) {
		// DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return createEvent(type, eventId, cal.getTime());
	}

	public XEvent createEvent(Event appEvent) {
		if (appEvent.getActivity() == null) {
			return null;
		}

		String type = appEvent.getActivity().getType().getName();
		String eventName = appEvent.getCompleteName();

		// DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return createEvent(type, eventName, cal.getTime());
	}

	public XEvent createEvent(String type, String name, Date timestamp) {
		XEvent event = null;

		if (!type.equals(Constants.FEATURE)) {
			type = (type.toLowerCase().equals("class_extension")) ? Constants.CLASS_EXTENSION
					: Constants.METHOD_EXTENSION;
			if (classesOnly && type.equals(Constants.METHOD_EXTENSION)) {
				return null;
			}
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

	public void generateLogForCluster(String logNamePrefix,
			Collection<FrameworkApplication> applications, float threshold) {
		this.log = initEventLog();

		XTrace trace = null;
		XEvent event = null;

		for (FrameworkApplication application : applications) {
			if (application == null) {
				System.out.println("Null application");
				continue;
			}
			String appName = application.getName();

			ReuseMinerApplicationTree tree = new ReuseMinerApplicationTree(
					application);
			List<CustomNode> traceNodes = tree
					.getTrace(VisitorStrategy.rootNode);

			if (traceNodes == null || traceNodes.size() == 0) {
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

			System.out.println("App: " + application.getName());
			for (CustomNode node : traceNodes) {
				if (node.getEvent() == null) {
					continue;
				}

				event = createEvent(node.getEvent());
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

		this.serialize(generateFilename(logNamePrefix), this.log);
	}

	private String generateFilename(String prefix) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd_HH-mm");
		String date = dateFormat.format(Calendar.getInstance().getTime());

		return String.format("%s-%s.xes", prefix, date);
	}

	public List<List<CustomNode>> getProcessTraces2(
			Collection<FrameworkApplication> applications) {
		this.log = initEventLog();
		List<List<CustomNode>> traces = new ArrayList<List<CustomNode>>();

		ClassToProcessMap processMap = new ClassToProcessMap();
		for (FrameworkApplication application : applications) {
			if (application == null) {
				System.out.println("Null application");
				continue;
			}
			String appName = application.getName();
			System.out.println("Mapping classes of application " + appName);

			ReuseMinerApplicationTree tree = new ReuseMinerApplicationTree(
					application);
			processMap.Visit(tree.getRootNode(), tree);
		}

		for (ClassProcess p : processMap.getMap().values()) {
			System.out.println(p.getName() + ": " + p.getOccurrences().size());
			generateLogForClassProcess(p);

			// System.out.println("Occurrences: ");
			// for (ClassProcessOccurrence occurrence: p.getOccurrences()) {
			// String list = occurrence.getListOfChildren();
			// if (list != null) {
			// System.out.println(occurrence.getListOfChildren());
			// }
			// }
		}

		return traces;
	}

	public void generateLogForClassProcess(ClassProcess p) {
		XLog log = initEventLog();

		int eventsCount = 0;

//		Map<String, List<ClassProcessOccurrence>> mapOccurrencesByApplication = new HashMap<String, List<ClassProcessOccurrence>>();
//		for (ClassProcessOccurrence occurrence : p.getOccurrences()) {
//			List<ClassProcessOccurrence> occurrences = mapOccurrencesByApplication
//					.get(occurrence.getApplicationName());
//			if (occurrences == null) {
//				occurrences = new ArrayList<ClassProcessOccurrence>();
//			}
//			occurrences.add(occurrence);
//			mapOccurrencesByApplication.put(occurrence.getApplicationName(), occurrences);
//		}
//
//		List<String> applicationNames = new ArrayList<String>();
//		applicationNames.addAll(mapOccurrencesByApplication.keySet());
//		
//		Collections.sort(applicationNames, new Comparator<String>() {
//			@Override
//			public int compare(String o1, String o2) {
//				return o1.toLowerCase().compareTo(o2.toLowerCase());
//			}
//		});
//		
//		
//		for (String appName: applicationNames) {
//			List<ClassProcessOccurrence> occurrences =  mapOccurrencesByApplication.get(appName);
//			List<String> allEvents = new ArrayList<String>();
//			
//			for (ClassProcessOccurrence occurrence: occurrences) {
//				List<String> occurrenceEvents = occurrence.getListOfChildren();
//				if (occurrenceEvents == null) continue;
//				
//				// Class Process is the last event in the trace
//				occurrenceEvents.add(p.getName());
//				eventsCount += occurrenceEvents.size();
//
//				allEvents.addAll(occurrenceEvents);
//			}
//			
//			if (allEvents.isEmpty()) continue;
//			
//			String traceName = String.format("%s-%s",
//					appName, p.getName());
//			XTrace trace = createLogTrace(traceName, p, allEvents);
//			
//			if (trace == null)
//				continue;
//
//			// Prevent adding empty traces
//			if (!trace.isEmpty()) {
//				log.add(trace);
//			}
//
//		}
		
		for (ClassProcessOccurrence occurrence : p.getOccurrences()) {
			List<String> occurrenceEvents = occurrence.getListOfChildren();
			if (occurrenceEvents == null)
				continue;

			// Class Process is the last event in the trace
			occurrenceEvents.add(p.getName());
			eventsCount += occurrenceEvents.size();

			String traceName = String.format("%s-%s",
					occurrence.getApplicationName(), p.getName());
			XTrace trace = createLogTrace(traceName, p, occurrenceEvents);

			if (trace == null)
				continue;

			// Prevent adding empty traces
			if (!trace.isEmpty()) {
				log.add(trace);
			}
		}

		if (eventsCount != p.getOccurrences().size()) {
			serialize(generateFilename(p.getName()), log);
		}
	}

	public XTrace createLogTrace(String traceName, ClassProcess p,
			Collection<String> events) {
		XTrace trace = createNewTrace(traceName);
		if (trace == null || events == null)
			return null;

		for (String eventName : events) {
			XEvent event = createEvent(Constants.CLASS_EXTENSION, eventName);
			if (event != null) {
				trace.add(event);
			}
		}

		// XEvent classProcessEvent = createEvent(Constants.CLASS_EXTENSION,
		// p.getName());
		// trace.add(classProcessEvent);

		return trace;
	}

	public List<List<CustomNode>> getProcessTraces(
			Collection<FrameworkApplication> applications) {
		this.log = initEventLog();
		List<List<CustomNode>> traces = new ArrayList<List<CustomNode>>();

		XTrace trace = null;
		XEvent event = null;

		for (FrameworkApplication application : applications) {
			if (application == null) {
				continue;
			}
			String appName = application.getName();

			ReuseMinerApplicationTree tree = new ReuseMinerApplicationTree(
					application);
			List<CustomNode> traceNodes = tree
					.getTrace(VisitorStrategy.rootNode);

			if (traceNodes.size() == 0) {
				continue;
			}
			traces.add(traceNodes);

			trace = createNewTrace(appName);
			if (trace == null) {
				continue;
			}

			int addedEvents = 0;

			for (CustomNode node : traceNodes) {

				if (node.getEvent() != null) {
					event = createEvent(node.getEvent());
				} else {
					event = createEvent(Constants.FEATURE, node.getEventId());
				}

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

		return traces;
	}

	public void getXESRepresentationFromProcess(
			Collection<FrameworkApplication> applications) {
		XTrace trace = null;
		XEvent event = null;

		for (FrameworkApplication application : applications) {
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
		this.serialize(filename, this.log);
	}

	public void serialize(String filename, XLog log) {
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