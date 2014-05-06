/**
 */
package minerv1.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import minerv1.Commit;
import minerv1.Event;
import minerv1.FrameworkApplication;
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
 * An implementation of the model object '<em><b>Framework Application</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link minerv1.impl.FrameworkApplicationImpl#getName <em>Name</em>}</li>
 *   <li>{@link minerv1.impl.FrameworkApplicationImpl#getRepositoryUrl <em>Repository Url</em>}</li>
 *   <li>{@link minerv1.impl.FrameworkApplicationImpl#getCommits <em>Commits</em>}</li>
 *   <li>{@link minerv1.impl.FrameworkApplicationImpl#isMine <em>Mine</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FrameworkApplicationImpl extends MinimalEObjectImpl.Container implements FrameworkApplication {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getRepositoryUrl() <em>Repository Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepositoryUrl()
	 * @generated
	 * @ordered
	 */
	protected static final String REPOSITORY_URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRepositoryUrl() <em>Repository Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepositoryUrl()
	 * @generated
	 * @ordered
	 */
	protected String repositoryUrl = REPOSITORY_URL_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCommits() <em>Commits</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommits()
	 * @generated
	 * @ordered
	 */
	protected EList<Commit> commits;

	/**
	 * The default value of the '{@link #mine() <em>Mine</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #mine()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MINE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #mine() <em>Mine</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #mine()
	 * @generated
	 * @ordered
	 */
	protected boolean mine = MINE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FrameworkApplicationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Minerv1Package.Literals.FRAMEWORK_APPLICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Minerv1Package.FRAMEWORK_APPLICATION__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRepositoryUrl() {
		return repositoryUrl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRepositoryUrl(String newRepositoryUrl) {
		String oldRepositoryUrl = repositoryUrl;
		repositoryUrl = newRepositoryUrl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Minerv1Package.FRAMEWORK_APPLICATION__REPOSITORY_URL, oldRepositoryUrl, repositoryUrl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Commit> getCommits() {
		if (commits == null) {
			commits = new EObjectContainmentEList<Commit>(Commit.class, this, Minerv1Package.FRAMEWORK_APPLICATION__COMMITS);
		}
		return commits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean mine() {
		return mine;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMine(boolean newMine) {
		boolean oldMine = mine;
		mine = newMine;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Minerv1Package.FRAMEWORK_APPLICATION__MINE, oldMine, mine));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Minerv1Package.FRAMEWORK_APPLICATION__COMMITS:
				return ((InternalEList<?>)getCommits()).basicRemove(otherEnd, msgs);
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
			case Minerv1Package.FRAMEWORK_APPLICATION__NAME:
				return getName();
			case Minerv1Package.FRAMEWORK_APPLICATION__REPOSITORY_URL:
				return getRepositoryUrl();
			case Minerv1Package.FRAMEWORK_APPLICATION__COMMITS:
				return getCommits();
			case Minerv1Package.FRAMEWORK_APPLICATION__MINE:
				return mine();
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
			case Minerv1Package.FRAMEWORK_APPLICATION__NAME:
				setName((String)newValue);
				return;
			case Minerv1Package.FRAMEWORK_APPLICATION__REPOSITORY_URL:
				setRepositoryUrl((String)newValue);
				return;
			case Minerv1Package.FRAMEWORK_APPLICATION__COMMITS:
				getCommits().clear();
				getCommits().addAll((Collection<? extends Commit>)newValue);
				return;
			case Minerv1Package.FRAMEWORK_APPLICATION__MINE:
				setMine((Boolean)newValue);
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
			case Minerv1Package.FRAMEWORK_APPLICATION__NAME:
				setName(NAME_EDEFAULT);
				return;
			case Minerv1Package.FRAMEWORK_APPLICATION__REPOSITORY_URL:
				setRepositoryUrl(REPOSITORY_URL_EDEFAULT);
				return;
			case Minerv1Package.FRAMEWORK_APPLICATION__COMMITS:
				getCommits().clear();
				return;
			case Minerv1Package.FRAMEWORK_APPLICATION__MINE:
				setMine(MINE_EDEFAULT);
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
			case Minerv1Package.FRAMEWORK_APPLICATION__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case Minerv1Package.FRAMEWORK_APPLICATION__REPOSITORY_URL:
				return REPOSITORY_URL_EDEFAULT == null ? repositoryUrl != null : !REPOSITORY_URL_EDEFAULT.equals(repositoryUrl);
			case Minerv1Package.FRAMEWORK_APPLICATION__COMMITS:
				return commits != null && !commits.isEmpty();
			case Minerv1Package.FRAMEWORK_APPLICATION__MINE:
				return mine != MINE_EDEFAULT;
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
		result.append(" (name: ");
		result.append(name);
		result.append(", repositoryUrl: ");
		result.append(repositoryUrl);
		result.append(", mine: ");
		result.append(mine);
		result.append(')');
		return result.toString();
	}

	public List<Event> getOrderedListOfEvents() {
		List<Event> events = new ArrayList<Event>();
		
		for (Commit commit: getCommits()) {
			events.addAll(commit.getEvents());
		}
		
		return events;
	}
	
} //FrameworkApplicationImpl
