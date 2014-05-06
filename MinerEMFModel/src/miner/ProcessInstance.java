/**
 */
package miner;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link miner.ProcessInstance#getName <em>Name</em>}</li>
 *   <li>{@link miner.ProcessInstance#getEvents <em>Events</em>}</li>
 *   <li>{@link miner.ProcessInstance#getCommits <em>Commits</em>}</li>
 * </ul>
 * </p>
 *
 * @see miner.MinerPackage#getProcessInstance()
 * @model
 * @generated
 */
public interface ProcessInstance extends EObject {
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
	 * @see miner.MinerPackage#getProcessInstance_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link miner.ProcessInstance#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Events</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Events</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Events</em>' containment reference.
	 * @see #setEvents(Event)
	 * @see miner.MinerPackage#getProcessInstance_Events()
	 * @model containment="true"
	 * @generated
	 */
	Event getEvents();

	/**
	 * Sets the value of the '{@link miner.ProcessInstance#getEvents <em>Events</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Events</em>' containment reference.
	 * @see #getEvents()
	 * @generated
	 */
	void setEvents(Event value);

	/**
	 * Returns the value of the '<em><b>Commits</b></em>' containment reference list.
	 * The list contents are of type {@link miner.Commit}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Commits</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Commits</em>' containment reference list.
	 * @see miner.MinerPackage#getProcessInstance_Commits()
	 * @model containment="true"
	 * @generated
	 */
	EList<Commit> getCommits();

} // ProcessInstance
