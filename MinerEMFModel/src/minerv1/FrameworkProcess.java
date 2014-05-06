/**
 */
package minerv1;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Framework Process</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link minerv1.FrameworkProcess#getName <em>Name</em>}</li>
 *   <li>{@link minerv1.FrameworkProcess#getApplications <em>Applications</em>}</li>
 *   <li>{@link minerv1.FrameworkProcess#getActivities <em>Activities</em>}</li>
 * </ul>
 * </p>
 *
 * @see minerv1.Minerv1Package#getFrameworkProcess()
 * @model
 * @generated
 */
public interface FrameworkProcess extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see minerv1.Minerv1Package#getFrameworkProcess_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link minerv1.FrameworkProcess#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Applications</b></em>' containment reference list.
	 * The list contents are of type {@link minerv1.FrameworkApplication}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Applications</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Applications</em>' containment reference list.
	 * @see minerv1.Minerv1Package#getFrameworkProcess_Applications()
	 * @model containment="true"
	 * @generated
	 */
	EList<FrameworkApplication> getApplications();

	/**
	 * Returns the value of the '<em><b>Activities</b></em>' containment reference list.
	 * The list contents are of type {@link minerv1.Activity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Activities</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Activities</em>' containment reference list.
	 * @see minerv1.Minerv1Package#getFrameworkProcess_Activities()
	 * @model containment="true"
	 * @generated
	 */
	EList<Activity> getActivities();

} // FrameworkProcess
