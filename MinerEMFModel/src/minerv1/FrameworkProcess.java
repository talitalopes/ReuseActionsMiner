/**
 */
package minerv1;

import java.util.Map;
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
 *   <li>{@link minerv1.FrameworkProcess#getDir <em>Dir</em>}</li>
 *   <li>{@link minerv1.FrameworkProcess#getKeyword <em>Keyword</em>}</li>
 *   <li>{@link minerv1.FrameworkProcess#getActivitiesMap <em>Activities Map</em>}</li>
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

	/**
	 * Returns the value of the '<em><b>Dir</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dir</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dir</em>' attribute.
	 * @see #setDir(String)
	 * @see minerv1.Minerv1Package#getFrameworkProcess_Dir()
	 * @model
	 * @generated
	 */
	String getDir();

	/**
	 * Sets the value of the '{@link minerv1.FrameworkProcess#getDir <em>Dir</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dir</em>' attribute.
	 * @see #getDir()
	 * @generated
	 */
	void setDir(String value);

	/**
	 * Returns the value of the '<em><b>Keyword</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Keyword</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Keyword</em>' attribute.
	 * @see #setKeyword(String)
	 * @see minerv1.Minerv1Package#getFrameworkProcess_Keyword()
	 * @model
	 * @generated
	 */
	String getKeyword();

	/**
	 * Sets the value of the '{@link minerv1.FrameworkProcess#getKeyword <em>Keyword</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Keyword</em>' attribute.
	 * @see #getKeyword()
	 * @generated
	 */
	void setKeyword(String value);

	/**
	 * Returns the value of the '<em><b>Activities Map</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Activities Map</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Activities Map</em>' attribute.
	 * @see #setActivitiesMap(Map)
	 * @see minerv1.Minerv1Package#getFrameworkProcess_ActivitiesMap()
	 * @model transient="true"
	 * @generated
	 */
	Map<String, Integer> getActivitiesMap();

	/**
	 * Sets the value of the '{@link minerv1.FrameworkProcess#getActivitiesMap <em>Activities Map</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Activities Map</em>' attribute.
	 * @see #getActivitiesMap()
	 * @generated
	 */
	void setActivitiesMap(Map<String, Integer> value);

	public boolean hasActivity(String name);
	
	public void populateActivitiesMap();
	
} // FrameworkProcess
