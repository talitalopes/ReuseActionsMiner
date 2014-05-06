/**
 *
 * $Id$
 */
package minerv1.validation;

import minerv1.Commit;
import org.eclipse.emf.common.util.EList;


/**
 * A sample validator interface for {@link minerv1.FrameworkApplication}.
 * This doesn't really do anything, and it's not a real EMF artifact.
 * It was generated by the org.eclipse.emf.examples.generator.validator plug-in to illustrate how EMF's code generator can be extended.
 * This can be disabled with -vmargs -Dorg.eclipse.emf.examples.generator.validator=false.
 */
public interface FrameworkApplicationValidator {
	boolean validate();

	boolean validateName(String value);
	boolean validateRepositoryUrl(String value);

	boolean validateCommits(EList<Commit> value);

	boolean validateMine(boolean value);

	boolean validateCommits(Commit value);
}
