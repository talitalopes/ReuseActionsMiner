/**
 */
package minerv1.impl;

import minerv1.Activity;
import minerv1.ActivityType;
import minerv1.Commit;
import minerv1.Event;
import minerv1.EventDependency;
import minerv1.FrameworkApplication;
import minerv1.FrameworkProcess;
import minerv1.Minerv1Factory;
import minerv1.Minerv1Package;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Minerv1PackageImpl extends EPackageImpl implements Minerv1Package {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass frameworkProcessEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass frameworkApplicationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass commitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass activityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eventEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eventDependencyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum activityTypeEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see minerv1.Minerv1Package#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private Minerv1PackageImpl() {
		super(eNS_URI, Minerv1Factory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link Minerv1Package#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static Minerv1Package init() {
		if (isInited) return (Minerv1Package)EPackage.Registry.INSTANCE.getEPackage(Minerv1Package.eNS_URI);

		// Obtain or create and register package
		Minerv1PackageImpl theMinerv1Package = (Minerv1PackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Minerv1PackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Minerv1PackageImpl());

		isInited = true;

		// Create package meta-data objects
		theMinerv1Package.createPackageContents();

		// Initialize created meta-data
		theMinerv1Package.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theMinerv1Package.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(Minerv1Package.eNS_URI, theMinerv1Package);
		return theMinerv1Package;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFrameworkProcess() {
		return frameworkProcessEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFrameworkProcess_Name() {
		return (EAttribute)frameworkProcessEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFrameworkProcess_Applications() {
		return (EReference)frameworkProcessEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFrameworkProcess_Activities() {
		return (EReference)frameworkProcessEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFrameworkProcess_Dir() {
		return (EAttribute)frameworkProcessEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFrameworkProcess_Keyword() {
		return (EAttribute)frameworkProcessEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFrameworkProcess_ActivitiesMap() {
		return (EAttribute)frameworkProcessEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFrameworkApplication() {
		return frameworkApplicationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFrameworkApplication_Name() {
		return (EAttribute)frameworkApplicationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFrameworkApplication_RepositoryUrl() {
		return (EAttribute)frameworkApplicationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFrameworkApplication_Commits() {
		return (EReference)frameworkApplicationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFrameworkApplication_Mine() {
		return (EAttribute)frameworkApplicationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCommit() {
		return commitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCommit_Id() {
		return (EAttribute)commitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCommit_Name() {
		return (EAttribute)commitEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCommit_Events() {
		return (EReference)commitEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getActivity() {
		return activityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getActivity_Id() {
		return (EAttribute)activityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getActivity_Name() {
		return (EAttribute)activityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getActivity_Type() {
		return (EAttribute)activityEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getActivity_PackageName() {
		return (EAttribute)activityEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEvent() {
		return eventEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEvent_Activity() {
		return (EReference)eventEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEvent_Date() {
		return (EAttribute)eventEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEvent_LifecycleStatus() {
		return (EAttribute)eventEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEvent_Id() {
		return (EAttribute)eventEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEvent_Dependencies() {
		return (EReference)eventEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEvent_WrittenToLog() {
		return (EAttribute)eventEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEventDependency() {
		return eventDependencyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEventDependency_Id() {
		return (EAttribute)eventDependencyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEventDependency_Event() {
		return (EReference)eventDependencyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getActivityType() {
		return activityTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Minerv1Factory getMinerv1Factory() {
		return (Minerv1Factory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		frameworkProcessEClass = createEClass(FRAMEWORK_PROCESS);
		createEAttribute(frameworkProcessEClass, FRAMEWORK_PROCESS__NAME);
		createEReference(frameworkProcessEClass, FRAMEWORK_PROCESS__APPLICATIONS);
		createEReference(frameworkProcessEClass, FRAMEWORK_PROCESS__ACTIVITIES);
		createEAttribute(frameworkProcessEClass, FRAMEWORK_PROCESS__DIR);
		createEAttribute(frameworkProcessEClass, FRAMEWORK_PROCESS__KEYWORD);
		createEAttribute(frameworkProcessEClass, FRAMEWORK_PROCESS__ACTIVITIES_MAP);

		frameworkApplicationEClass = createEClass(FRAMEWORK_APPLICATION);
		createEAttribute(frameworkApplicationEClass, FRAMEWORK_APPLICATION__NAME);
		createEAttribute(frameworkApplicationEClass, FRAMEWORK_APPLICATION__REPOSITORY_URL);
		createEReference(frameworkApplicationEClass, FRAMEWORK_APPLICATION__COMMITS);
		createEAttribute(frameworkApplicationEClass, FRAMEWORK_APPLICATION__MINE);

		commitEClass = createEClass(COMMIT);
		createEAttribute(commitEClass, COMMIT__ID);
		createEAttribute(commitEClass, COMMIT__NAME);
		createEReference(commitEClass, COMMIT__EVENTS);

		activityEClass = createEClass(ACTIVITY);
		createEAttribute(activityEClass, ACTIVITY__ID);
		createEAttribute(activityEClass, ACTIVITY__NAME);
		createEAttribute(activityEClass, ACTIVITY__TYPE);
		createEAttribute(activityEClass, ACTIVITY__PACKAGE_NAME);

		eventEClass = createEClass(EVENT);
		createEReference(eventEClass, EVENT__ACTIVITY);
		createEAttribute(eventEClass, EVENT__DATE);
		createEAttribute(eventEClass, EVENT__LIFECYCLE_STATUS);
		createEAttribute(eventEClass, EVENT__ID);
		createEReference(eventEClass, EVENT__DEPENDENCIES);
		createEAttribute(eventEClass, EVENT__WRITTEN_TO_LOG);

		eventDependencyEClass = createEClass(EVENT_DEPENDENCY);
		createEAttribute(eventDependencyEClass, EVENT_DEPENDENCY__ID);
		createEReference(eventDependencyEClass, EVENT_DEPENDENCY__EVENT);

		// Create enums
		activityTypeEEnum = createEEnum(ACTIVITY_TYPE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(frameworkProcessEClass, FrameworkProcess.class, "FrameworkProcess", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFrameworkProcess_Name(), ecorePackage.getEString(), "name", null, 0, 1, FrameworkProcess.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFrameworkProcess_Applications(), this.getFrameworkApplication(), null, "applications", null, 0, -1, FrameworkProcess.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFrameworkProcess_Activities(), this.getActivity(), null, "activities", null, 0, -1, FrameworkProcess.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFrameworkProcess_Dir(), ecorePackage.getEString(), "dir", null, 0, 1, FrameworkProcess.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFrameworkProcess_Keyword(), ecorePackage.getEString(), "keyword", null, 0, 1, FrameworkProcess.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		EGenericType g1 = createEGenericType(ecorePackage.getEMap());
		EGenericType g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		initEAttribute(getFrameworkProcess_ActivitiesMap(), g1, "activitiesMap", null, 0, 1, FrameworkProcess.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(frameworkApplicationEClass, FrameworkApplication.class, "FrameworkApplication", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFrameworkApplication_Name(), ecorePackage.getEString(), "name", null, 0, 1, FrameworkApplication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFrameworkApplication_RepositoryUrl(), ecorePackage.getEString(), "repositoryUrl", null, 0, 1, FrameworkApplication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFrameworkApplication_Commits(), this.getCommit(), null, "commits", null, 0, -1, FrameworkApplication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFrameworkApplication_Mine(), ecorePackage.getEBoolean(), "mine", null, 0, 1, FrameworkApplication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(commitEClass, Commit.class, "Commit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCommit_Id(), ecorePackage.getEString(), "id", null, 0, 1, Commit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCommit_Name(), ecorePackage.getEString(), "name", null, 0, 1, Commit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCommit_Events(), this.getEvent(), null, "events", null, 0, -1, Commit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(activityEClass, Activity.class, "Activity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getActivity_Id(), ecorePackage.getEString(), "id", null, 0, 1, Activity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getActivity_Name(), ecorePackage.getEString(), "name", null, 0, 1, Activity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getActivity_Type(), this.getActivityType(), "type", null, 0, 1, Activity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getActivity_PackageName(), ecorePackage.getEString(), "packageName", null, 0, 1, Activity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eventEClass, Event.class, "Event", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEvent_Activity(), this.getActivity(), null, "Activity", null, 0, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEvent_Date(), ecorePackage.getEDate(), "date", null, 0, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEvent_LifecycleStatus(), ecorePackage.getEString(), "lifecycleStatus", null, 0, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEvent_Id(), ecorePackage.getEString(), "id", null, 0, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEvent_Dependencies(), this.getEventDependency(), null, "dependencies", null, 0, -1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEvent_WrittenToLog(), ecorePackage.getEBoolean(), "writtenToLog", null, 0, 1, Event.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eventDependencyEClass, EventDependency.class, "EventDependency", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEventDependency_Id(), ecorePackage.getEString(), "id", null, 0, 1, EventDependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEventDependency_Event(), this.getEvent(), null, "event", null, 0, 1, EventDependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(activityTypeEEnum, ActivityType.class, "ActivityType");
		addEEnumLiteral(activityTypeEEnum, ActivityType.METHOD_EXTENSION);
		addEEnumLiteral(activityTypeEEnum, ActivityType.CLASS_EXTENSION);
		addEEnumLiteral(activityTypeEEnum, ActivityType.OVERRIDES_METHOD);

		// Create resource
		createResource(eNS_URI);
	}

} //Minerv1PackageImpl
