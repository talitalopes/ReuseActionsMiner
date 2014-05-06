/**
 */
package minerv1;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see minerv1.Minerv1Factory
 * @model kind="package"
 * @generated
 */
public interface Minerv1Package extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "minerv1";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://prisma.cos.ufrj.br/processreuseminerv1";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "br.ufrj.cos.prisma.miner.v1";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Minerv1Package eINSTANCE = minerv1.impl.Minerv1PackageImpl.init();

	/**
	 * The meta object id for the '{@link minerv1.impl.FrameworkProcessImpl <em>Framework Process</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see minerv1.impl.FrameworkProcessImpl
	 * @see minerv1.impl.Minerv1PackageImpl#getFrameworkProcess()
	 * @generated
	 */
	int FRAMEWORK_PROCESS = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRAMEWORK_PROCESS__NAME = 0;

	/**
	 * The feature id for the '<em><b>Applications</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRAMEWORK_PROCESS__APPLICATIONS = 1;

	/**
	 * The feature id for the '<em><b>Activities</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRAMEWORK_PROCESS__ACTIVITIES = 2;

	/**
	 * The number of structural features of the '<em>Framework Process</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRAMEWORK_PROCESS_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Framework Process</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRAMEWORK_PROCESS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link minerv1.impl.FrameworkApplicationImpl <em>Framework Application</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see minerv1.impl.FrameworkApplicationImpl
	 * @see minerv1.impl.Minerv1PackageImpl#getFrameworkApplication()
	 * @generated
	 */
	int FRAMEWORK_APPLICATION = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRAMEWORK_APPLICATION__NAME = 0;

	/**
	 * The feature id for the '<em><b>Repository Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRAMEWORK_APPLICATION__REPOSITORY_URL = 1;

	/**
	 * The feature id for the '<em><b>Commits</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRAMEWORK_APPLICATION__COMMITS = 2;

	/**
	 * The feature id for the '<em><b>Mine</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRAMEWORK_APPLICATION__MINE = 3;

	/**
	 * The number of structural features of the '<em>Framework Application</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRAMEWORK_APPLICATION_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Framework Application</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FRAMEWORK_APPLICATION_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link minerv1.impl.CommitImpl <em>Commit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see minerv1.impl.CommitImpl
	 * @see minerv1.impl.Minerv1PackageImpl#getCommit()
	 * @generated
	 */
	int COMMIT = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMIT__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMIT__NAME = 1;

	/**
	 * The feature id for the '<em><b>Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMIT__EVENTS = 2;

	/**
	 * The number of structural features of the '<em>Commit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMIT_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Commit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMIT_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link minerv1.impl.ActivityImpl <em>Activity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see minerv1.impl.ActivityImpl
	 * @see minerv1.impl.Minerv1PackageImpl#getActivity()
	 * @generated
	 */
	int ACTIVITY = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIVITY__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIVITY__NAME = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIVITY__TYPE = 2;

	/**
	 * The number of structural features of the '<em>Activity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIVITY_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Activity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIVITY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link minerv1.impl.EventImpl <em>Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see minerv1.impl.EventImpl
	 * @see minerv1.impl.Minerv1PackageImpl#getEvent()
	 * @generated
	 */
	int EVENT = 4;

	/**
	 * The feature id for the '<em><b>Activity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__ACTIVITY = 0;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__DATE = 1;

	/**
	 * The feature id for the '<em><b>Lifecycle Status</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__LIFECYCLE_STATUS = 2;

	/**
	 * The number of structural features of the '<em>Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link minerv1.ActivityType <em>Activity Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see minerv1.ActivityType
	 * @see minerv1.impl.Minerv1PackageImpl#getActivityType()
	 * @generated
	 */
	int ACTIVITY_TYPE = 5;


	/**
	 * Returns the meta object for class '{@link minerv1.FrameworkProcess <em>Framework Process</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Framework Process</em>'.
	 * @see minerv1.FrameworkProcess
	 * @generated
	 */
	EClass getFrameworkProcess();

	/**
	 * Returns the meta object for the attribute '{@link minerv1.FrameworkProcess#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see minerv1.FrameworkProcess#getName()
	 * @see #getFrameworkProcess()
	 * @generated
	 */
	EAttribute getFrameworkProcess_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link minerv1.FrameworkProcess#getApplications <em>Applications</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Applications</em>'.
	 * @see minerv1.FrameworkProcess#getApplications()
	 * @see #getFrameworkProcess()
	 * @generated
	 */
	EReference getFrameworkProcess_Applications();

	/**
	 * Returns the meta object for the containment reference list '{@link minerv1.FrameworkProcess#getActivities <em>Activities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Activities</em>'.
	 * @see minerv1.FrameworkProcess#getActivities()
	 * @see #getFrameworkProcess()
	 * @generated
	 */
	EReference getFrameworkProcess_Activities();

	/**
	 * Returns the meta object for class '{@link minerv1.FrameworkApplication <em>Framework Application</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Framework Application</em>'.
	 * @see minerv1.FrameworkApplication
	 * @generated
	 */
	EClass getFrameworkApplication();

	/**
	 * Returns the meta object for the attribute '{@link minerv1.FrameworkApplication#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see minerv1.FrameworkApplication#getName()
	 * @see #getFrameworkApplication()
	 * @generated
	 */
	EAttribute getFrameworkApplication_Name();

	/**
	 * Returns the meta object for the attribute '{@link minerv1.FrameworkApplication#getRepositoryUrl <em>Repository Url</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Repository Url</em>'.
	 * @see minerv1.FrameworkApplication#getRepositoryUrl()
	 * @see #getFrameworkApplication()
	 * @generated
	 */
	EAttribute getFrameworkApplication_RepositoryUrl();

	/**
	 * Returns the meta object for the containment reference list '{@link minerv1.FrameworkApplication#getCommits <em>Commits</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Commits</em>'.
	 * @see minerv1.FrameworkApplication#getCommits()
	 * @see #getFrameworkApplication()
	 * @generated
	 */
	EReference getFrameworkApplication_Commits();

	/**
	 * Returns the meta object for the attribute '{@link minerv1.FrameworkApplication#isMine <em>Mine</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mine</em>'.
	 * @see minerv1.FrameworkApplication#mine()
	 * @see #getFrameworkApplication()
	 * @generated
	 */
	EAttribute getFrameworkApplication_Mine();

	/**
	 * Returns the meta object for class '{@link minerv1.Commit <em>Commit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Commit</em>'.
	 * @see minerv1.Commit
	 * @generated
	 */
	EClass getCommit();

	/**
	 * Returns the meta object for the attribute '{@link minerv1.Commit#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see minerv1.Commit#getId()
	 * @see #getCommit()
	 * @generated
	 */
	EAttribute getCommit_Id();

	/**
	 * Returns the meta object for the attribute '{@link minerv1.Commit#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see minerv1.Commit#getName()
	 * @see #getCommit()
	 * @generated
	 */
	EAttribute getCommit_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link minerv1.Commit#getEvents <em>Events</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Events</em>'.
	 * @see minerv1.Commit#getEvents()
	 * @see #getCommit()
	 * @generated
	 */
	EReference getCommit_Events();

	/**
	 * Returns the meta object for class '{@link minerv1.Activity <em>Activity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Activity</em>'.
	 * @see minerv1.Activity
	 * @generated
	 */
	EClass getActivity();

	/**
	 * Returns the meta object for the attribute '{@link minerv1.Activity#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see minerv1.Activity#getId()
	 * @see #getActivity()
	 * @generated
	 */
	EAttribute getActivity_Id();

	/**
	 * Returns the meta object for the attribute '{@link minerv1.Activity#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see minerv1.Activity#getName()
	 * @see #getActivity()
	 * @generated
	 */
	EAttribute getActivity_Name();

	/**
	 * Returns the meta object for the attribute '{@link minerv1.Activity#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see minerv1.Activity#getType()
	 * @see #getActivity()
	 * @generated
	 */
	EAttribute getActivity_Type();

	/**
	 * Returns the meta object for class '{@link minerv1.Event <em>Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Event</em>'.
	 * @see minerv1.Event
	 * @generated
	 */
	EClass getEvent();

	/**
	 * Returns the meta object for the reference '{@link minerv1.Event#getActivity <em>Activity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Activity</em>'.
	 * @see minerv1.Event#getActivity()
	 * @see #getEvent()
	 * @generated
	 */
	EReference getEvent_Activity();

	/**
	 * Returns the meta object for the attribute '{@link minerv1.Event#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see minerv1.Event#getDate()
	 * @see #getEvent()
	 * @generated
	 */
	EAttribute getEvent_Date();

	/**
	 * Returns the meta object for the attribute '{@link minerv1.Event#getLifecycleStatus <em>Lifecycle Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lifecycle Status</em>'.
	 * @see minerv1.Event#getLifecycleStatus()
	 * @see #getEvent()
	 * @generated
	 */
	EAttribute getEvent_LifecycleStatus();

	/**
	 * Returns the meta object for enum '{@link minerv1.ActivityType <em>Activity Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Activity Type</em>'.
	 * @see minerv1.ActivityType
	 * @generated
	 */
	EEnum getActivityType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	Minerv1Factory getMinerv1Factory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link minerv1.impl.FrameworkProcessImpl <em>Framework Process</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see minerv1.impl.FrameworkProcessImpl
		 * @see minerv1.impl.Minerv1PackageImpl#getFrameworkProcess()
		 * @generated
		 */
		EClass FRAMEWORK_PROCESS = eINSTANCE.getFrameworkProcess();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FRAMEWORK_PROCESS__NAME = eINSTANCE.getFrameworkProcess_Name();

		/**
		 * The meta object literal for the '<em><b>Applications</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FRAMEWORK_PROCESS__APPLICATIONS = eINSTANCE.getFrameworkProcess_Applications();

		/**
		 * The meta object literal for the '<em><b>Activities</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FRAMEWORK_PROCESS__ACTIVITIES = eINSTANCE.getFrameworkProcess_Activities();

		/**
		 * The meta object literal for the '{@link minerv1.impl.FrameworkApplicationImpl <em>Framework Application</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see minerv1.impl.FrameworkApplicationImpl
		 * @see minerv1.impl.Minerv1PackageImpl#getFrameworkApplication()
		 * @generated
		 */
		EClass FRAMEWORK_APPLICATION = eINSTANCE.getFrameworkApplication();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FRAMEWORK_APPLICATION__NAME = eINSTANCE.getFrameworkApplication_Name();

		/**
		 * The meta object literal for the '<em><b>Repository Url</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FRAMEWORK_APPLICATION__REPOSITORY_URL = eINSTANCE.getFrameworkApplication_RepositoryUrl();

		/**
		 * The meta object literal for the '<em><b>Commits</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FRAMEWORK_APPLICATION__COMMITS = eINSTANCE.getFrameworkApplication_Commits();

		/**
		 * The meta object literal for the '<em><b>Mine</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FRAMEWORK_APPLICATION__MINE = eINSTANCE.getFrameworkApplication_Mine();

		/**
		 * The meta object literal for the '{@link minerv1.impl.CommitImpl <em>Commit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see minerv1.impl.CommitImpl
		 * @see minerv1.impl.Minerv1PackageImpl#getCommit()
		 * @generated
		 */
		EClass COMMIT = eINSTANCE.getCommit();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMIT__ID = eINSTANCE.getCommit_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMIT__NAME = eINSTANCE.getCommit_Name();

		/**
		 * The meta object literal for the '<em><b>Events</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMMIT__EVENTS = eINSTANCE.getCommit_Events();

		/**
		 * The meta object literal for the '{@link minerv1.impl.ActivityImpl <em>Activity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see minerv1.impl.ActivityImpl
		 * @see minerv1.impl.Minerv1PackageImpl#getActivity()
		 * @generated
		 */
		EClass ACTIVITY = eINSTANCE.getActivity();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ACTIVITY__ID = eINSTANCE.getActivity_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ACTIVITY__NAME = eINSTANCE.getActivity_Name();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ACTIVITY__TYPE = eINSTANCE.getActivity_Type();

		/**
		 * The meta object literal for the '{@link minerv1.impl.EventImpl <em>Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see minerv1.impl.EventImpl
		 * @see minerv1.impl.Minerv1PackageImpl#getEvent()
		 * @generated
		 */
		EClass EVENT = eINSTANCE.getEvent();

		/**
		 * The meta object literal for the '<em><b>Activity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EVENT__ACTIVITY = eINSTANCE.getEvent_Activity();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EVENT__DATE = eINSTANCE.getEvent_Date();

		/**
		 * The meta object literal for the '<em><b>Lifecycle Status</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EVENT__LIFECYCLE_STATUS = eINSTANCE.getEvent_LifecycleStatus();

		/**
		 * The meta object literal for the '{@link minerv1.ActivityType <em>Activity Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see minerv1.ActivityType
		 * @see minerv1.impl.Minerv1PackageImpl#getActivityType()
		 * @generated
		 */
		EEnum ACTIVITY_TYPE = eINSTANCE.getActivityType();

	}

} //Minerv1Package
