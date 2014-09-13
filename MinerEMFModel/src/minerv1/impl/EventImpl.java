/**
 */
package minerv1.impl;

import java.util.Collection;
import java.util.Date;

import minerv1.Activity;
import minerv1.Event;
import minerv1.EventDependency;
import minerv1.Minerv1Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link minerv1.impl.EventImpl#getActivity <em>Activity</em>}</li>
 *   <li>{@link minerv1.impl.EventImpl#getDate <em>Date</em>}</li>
 *   <li>{@link minerv1.impl.EventImpl#getLifecycleStatus <em>Lifecycle Status</em>}</li>
 *   <li>{@link minerv1.impl.EventImpl#getId <em>Id</em>}</li>
 *   <li>{@link minerv1.impl.EventImpl#getDependencies <em>Dependencies</em>}</li>
 *   <li>{@link minerv1.impl.EventImpl#isWrittenToLog <em>Written To Log</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EventImpl extends MinimalEObjectImpl.Container implements Event {
	/**
	 * The cached value of the '{@link #getActivity() <em>Activity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActivity()
	 * @generated
	 * @ordered
	 */
	protected Activity activity;

	/**
	 * The default value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected static final Date DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected Date date = DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLifecycleStatus() <em>Lifecycle Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLifecycleStatus()
	 * @generated
	 * @ordered
	 */
	protected static final String LIFECYCLE_STATUS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLifecycleStatus() <em>Lifecycle Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLifecycleStatus()
	 * @generated
	 * @ordered
	 */
	protected String lifecycleStatus = LIFECYCLE_STATUS_EDEFAULT;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDependencies() <em>Dependencies</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependencies()
	 * @generated
	 * @ordered
	 */
	protected EList<EventDependency> dependencies;

	/**
	 * The default value of the '{@link #isWrittenToLog() <em>Written To Log</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWrittenToLog()
	 * @generated
	 * @ordered
	 */
	protected static final boolean WRITTEN_TO_LOG_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isWrittenToLog() <em>Written To Log</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWrittenToLog()
	 * @generated
	 * @ordered
	 */
	protected boolean writtenToLog = WRITTEN_TO_LOG_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EventImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Minerv1Package.Literals.EVENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Activity getActivity() {
		if (activity != null && activity.eIsProxy()) {
			InternalEObject oldActivity = (InternalEObject)activity;
			activity = (Activity)eResolveProxy(oldActivity);
			if (activity != oldActivity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, Minerv1Package.EVENT__ACTIVITY, oldActivity, activity));
			}
		}
		return activity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Activity basicGetActivity() {
		return activity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setActivity(Activity newActivity) {
		Activity oldActivity = activity;
		activity = newActivity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Minerv1Package.EVENT__ACTIVITY, oldActivity, activity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDate(Date newDate) {
		Date oldDate = date;
		date = newDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Minerv1Package.EVENT__DATE, oldDate, date));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLifecycleStatus() {
		return lifecycleStatus;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLifecycleStatus(String newLifecycleStatus) {
		String oldLifecycleStatus = lifecycleStatus;
		lifecycleStatus = newLifecycleStatus;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Minerv1Package.EVENT__LIFECYCLE_STATUS, oldLifecycleStatus, lifecycleStatus));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Minerv1Package.EVENT__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EventDependency> getDependencies() {
		if (dependencies == null) {
			dependencies = new EObjectContainmentEList<EventDependency>(EventDependency.class, this, Minerv1Package.EVENT__DEPENDENCIES);
		}
		return dependencies;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isWrittenToLog() {
		return writtenToLog;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWrittenToLog(boolean newWrittenToLog) {
		boolean oldWrittenToLog = writtenToLog;
		writtenToLog = newWrittenToLog;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Minerv1Package.EVENT__WRITTEN_TO_LOG, oldWrittenToLog, writtenToLog));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Minerv1Package.EVENT__DEPENDENCIES:
				return ((InternalEList<?>)getDependencies()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Minerv1Package.EVENT__ACTIVITY:
				if (resolve) return getActivity();
				return basicGetActivity();
			case Minerv1Package.EVENT__DATE:
				return getDate();
			case Minerv1Package.EVENT__LIFECYCLE_STATUS:
				return getLifecycleStatus();
			case Minerv1Package.EVENT__ID:
				return getId();
			case Minerv1Package.EVENT__DEPENDENCIES:
				return getDependencies();
			case Minerv1Package.EVENT__WRITTEN_TO_LOG:
				return isWrittenToLog();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Minerv1Package.EVENT__ACTIVITY:
				setActivity((Activity)newValue);
				return;
			case Minerv1Package.EVENT__DATE:
				setDate((Date)newValue);
				return;
			case Minerv1Package.EVENT__LIFECYCLE_STATUS:
				setLifecycleStatus((String)newValue);
				return;
			case Minerv1Package.EVENT__ID:
				setId((String)newValue);
				return;
			case Minerv1Package.EVENT__DEPENDENCIES:
				getDependencies().clear();
				getDependencies().addAll((Collection<? extends EventDependency>)newValue);
				return;
			case Minerv1Package.EVENT__WRITTEN_TO_LOG:
				setWrittenToLog((Boolean)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case Minerv1Package.EVENT__ACTIVITY:
				setActivity((Activity)null);
				return;
			case Minerv1Package.EVENT__DATE:
				setDate(DATE_EDEFAULT);
				return;
			case Minerv1Package.EVENT__LIFECYCLE_STATUS:
				setLifecycleStatus(LIFECYCLE_STATUS_EDEFAULT);
				return;
			case Minerv1Package.EVENT__ID:
				setId(ID_EDEFAULT);
				return;
			case Minerv1Package.EVENT__DEPENDENCIES:
				getDependencies().clear();
				return;
			case Minerv1Package.EVENT__WRITTEN_TO_LOG:
				setWrittenToLog(WRITTEN_TO_LOG_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case Minerv1Package.EVENT__ACTIVITY:
				return activity != null;
			case Minerv1Package.EVENT__DATE:
				return DATE_EDEFAULT == null ? date != null : !DATE_EDEFAULT.equals(date);
			case Minerv1Package.EVENT__LIFECYCLE_STATUS:
				return LIFECYCLE_STATUS_EDEFAULT == null ? lifecycleStatus != null : !LIFECYCLE_STATUS_EDEFAULT.equals(lifecycleStatus);
			case Minerv1Package.EVENT__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case Minerv1Package.EVENT__DEPENDENCIES:
				return dependencies != null && !dependencies.isEmpty();
			case Minerv1Package.EVENT__WRITTEN_TO_LOG:
				return writtenToLog != WRITTEN_TO_LOG_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (date: ");
		result.append(date);
		result.append(", lifecycleStatus: ");
		result.append(lifecycleStatus);
		result.append(", id: ");
		result.append(id);
		result.append(", writtenToLog: ");
		result.append(writtenToLog);
		result.append(')');
		return result.toString();
	}

} //EventImpl
