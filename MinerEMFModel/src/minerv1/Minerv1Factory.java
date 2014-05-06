/**
 */
package minerv1;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see minerv1.Minerv1Package
 * @generated
 */
public interface Minerv1Factory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Minerv1Factory eINSTANCE = minerv1.impl.Minerv1FactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Framework Process</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Framework Process</em>'.
	 * @generated
	 */
	FrameworkProcess createFrameworkProcess();

	/**
	 * Returns a new object of class '<em>Framework Application</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Framework Application</em>'.
	 * @generated
	 */
	FrameworkApplication createFrameworkApplication();

	/**
	 * Returns a new object of class '<em>Commit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Commit</em>'.
	 * @generated
	 */
	Commit createCommit();

	/**
	 * Returns a new object of class '<em>Activity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Activity</em>'.
	 * @generated
	 */
	Activity createActivity();

	/**
	 * Returns a new object of class '<em>Event</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Event</em>'.
	 * @generated
	 */
	Event createEvent();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	Minerv1Package getMinerv1Package();

} //Minerv1Factory
