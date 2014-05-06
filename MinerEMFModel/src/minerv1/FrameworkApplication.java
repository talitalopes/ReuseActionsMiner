/**
 */
package minerv1;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Framework Application</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link minerv1.FrameworkApplication#getName <em>Name</em>}</li>
 *   <li>{@link minerv1.FrameworkApplication#getRepositoryUrl <em>Repository Url</em>}</li>
 *   <li>{@link minerv1.FrameworkApplication#getCommits <em>Commits</em>}</li>
 *   <li>{@link minerv1.FrameworkApplication#isMine <em>Mine</em>}</li>
 * </ul>
 * </p>
 *
 * @see minerv1.Minerv1Package#getFrameworkApplication()
 * @model
 * @generated
 */
public interface FrameworkApplication extends EObject {
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
	 * @see minerv1.Minerv1Package#getFrameworkApplication_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link minerv1.FrameworkApplication#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Repository Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Repository Url</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Repository Url</em>' attribute.
	 * @see #setRepositoryUrl(String)
	 * @see minerv1.Minerv1Package#getFrameworkApplication_RepositoryUrl()
	 * @model
	 * @generated
	 */
	String getRepositoryUrl();

	/**
	 * Sets the value of the '{@link minerv1.FrameworkApplication#getRepositoryUrl <em>Repository Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Repository Url</em>' attribute.
	 * @see #getRepositoryUrl()
	 * @generated
	 */
	void setRepositoryUrl(String value);

	/**
	 * Returns the value of the '<em><b>Commits</b></em>' containment reference list.
	 * The list contents are of type {@link minerv1.Commit}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Commits</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Commits</em>' containment reference list.
	 * @see minerv1.Minerv1Package#getFrameworkApplication_Commits()
	 * @model containment="true"
	 * @generated
	 */
	EList<Commit> getCommits();

	/**
	 * Returns the value of the '<em><b>Mine</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mine</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mine</em>' attribute.
	 * @see #setMine(boolean)
	 * @see minerv1.Minerv1Package#getFrameworkApplication_Mine()
	 * @model
	 * @generated
	 */
	boolean mine();

	/**
	 * Sets the value of the '{@link minerv1.FrameworkApplication#isMine <em>Mine</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mine</em>' attribute.
	 * @see #mine()
	 * @generated
	 */
	void setMine(boolean value);
	
	List<Event> getOrderedListOfEvents();

} // FrameworkApplication
