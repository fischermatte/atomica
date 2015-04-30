package com.geolud.atomica.logic;

import com.geolud.atomica.objects.AtomToken;
import com.geolud.atomica.objects.GameSituation;
import com.geolud.atomica.objects.Molecule;

import java.util.ArrayList;

/**
 * The class is responsible for detecting all Molecules in a game situation.
 *
 * @author Georg Ludewig
 */
public class MoleculeDetector {
    /**
     * Holds the situation which has to be scanned.
     */
    private GameSituation gameSituation = null;

    /**
     * Keeps all detected Molecules.
     */
    private ArrayList<Molecule> detectedMolecules = null;

    /**
     * Keeps track of the biggest Molecule while detecting a Molecule for a
     * certain Atom.
     */
    private Molecule maxSizeMolecule;

    /**
     * Creates a MoleculeDetector for the given <code>gameSituation</code>.
     *
     * @param gameSituation the situation to be scanned
     */
    public MoleculeDetector(GameSituation gameSituation) {
        this.gameSituation = gameSituation;
        detectedMolecules = new ArrayList<Molecule>();
    }

    /**
     * Tries to detect a bigger Molecule than the given <code>m</code>. This
     * method will be called recursively for each found Molecule.
     *
     * @param m the initial Molecule from where to start to detect a bigger one.
     */
    private void detectMaxSizeMolecule(Molecule m) {
        if (m == null)
            return;

        Molecule mEast = detectMoleculeEast(m);
        Molecule mSouth = detectMoleculeSouth(m);
        Molecule mSquare = detectMoleculeSquare(mEast, mSouth);

        // put east molecule as currently biggest
        if (mEast != null && mEast.size() > maxSizeMolecule.size()) {
            maxSizeMolecule = mEast;
        }

        // put south molecule as currently biggest
        if (mSouth != null && mSouth.size() > maxSizeMolecule.size()) {
            maxSizeMolecule = mSouth;
        }

        // put the square molecule as currently biggest
        if (mSquare != null && mSquare.size() > maxSizeMolecule.size()) {
            maxSizeMolecule = mSquare;
        }

        // now recursively search for bigger molecules
        detectMaxSizeMolecule(mEast);
        detectMaxSizeMolecule(mSouth);
        detectMaxSizeMolecule(mSquare);
    }

    /**
     * Detects the Molecule with maximum size the Atom is involved in.
     * <p/>
     * <p>
     * If the Atom is not already used by another Molecule the scan algorithm
     * starts. It first tries to build a 2*2 Molecule. If that's successful it
     * recursively tries to grow. The scan direction is only east and south, as
     * the MoleculeDetector scans the whole game situation.
     * </p>
     *
     * @param startAtom the atom where to start the Molecule detection
     */
    private void detectMolecule(AtomToken startAtom) {
        maxSizeMolecule = null;

        if (isUsed(startAtom))
            return;

        Molecule m = detectSimpleMolecule(startAtom);
        if (m != null) {
            maxSizeMolecule = m;
            detectMaxSizeMolecule(m);
        }
    }

    /**
     * Tries to detect a Molecule with a bigger size than the given
     * <code>m</code> in east direction.
     *
     * @param m the initial Molecule from where to start to detect a bigger one.
     * @return the Molecule with a bigger size (if found)
     */
    private Molecule detectMoleculeEast(Molecule m) {
        Molecule mEast = null;

        int minRow = m.getMinRow();
        int maxRow = m.getMaxRow();
        int maxCol = m.getMaxCol();

        // constructs a copy of the given molecule
        Molecule tmp = new Molecule(m);

        // search all fields in east direction for equal atoms
        for (int row = minRow; row <= maxRow; row++) {
            AtomToken atomE = gameSituation.queryAtom(maxCol + 1, row, m
                    .getColorIndex());
            if (atomE != null && !isUsed(atomE)) {
                tmp.addAtom(atomE);
            } else {
                tmp = null;
                break;
            }
        }
        // a bigger molecule was found, so assign it as return value
        if (tmp != null && tmp.isValid()) {
            mEast = tmp;
        }

        return mEast;
    }

    /**
     * Scans the whole game situation for Molecules and returns them in an
     * ArrayList.
     *
     * @return a list of all detected Molecules
     */
    public ArrayList<Molecule> detectMolecules() {
        ArrayList<AtomToken> atoms = gameSituation.getAtoms();
        for (AtomToken atom : atoms) {
            detectMolecule(atom);
            if (maxSizeMolecule != null) {
                detectedMolecules.add(maxSizeMolecule);
                maxSizeMolecule = null;
            }
        }

        return detectedMolecules;
    }

    /**
     * Tries to detect a Molecule with a bigger size than the given
     * <code>m</code> in south direction.
     *
     * @param m the initial Molecule from where to start to detect a bigger one.
     * @return the Molecule with a bigger size (if found)
     */
    private Molecule detectMoleculeSouth(Molecule m) {
        Molecule mSouth = null;

        int maxRow = m.getMaxRow();
        int maxCol = m.getMaxCol();
        int minCol = m.getMinCol();

        // constructs a copy of the given molecule
        Molecule tmp = new Molecule(m);

        // search all fields in south direction for equal atoms
        for (int col = minCol; col <= maxCol; col++) {
            AtomToken atomS = gameSituation.queryAtom(col, maxRow + 1, m
                    .getColorIndex());
            if (atomS != null && !isUsed(atomS)) {
                tmp.addAtom(atomS);
            } else {
                tmp = null;
                break;
            }
        }

        // a bigger molecule was found, so assign it as return value
        if (tmp != null && tmp.isValid()) {
            mSouth = tmp;
        }

        return mSouth;
    }

    /**
     * Scans the square, where mEast defines the columns and mSouth the rows. A
     * valid Molecule must have Atoms at all that Fields.
     *
     * @param mEast  the Molecule defining the columns
     * @param mSouth the Molecule defining the rows
     * @return the Molecule in the given area. if non exist: null.
     */
    private Molecule detectMoleculeSquare(Molecule mEast, Molecule mSouth) {
        if (mEast == null || mSouth == null)
            return null;

        int col = mEast.getMaxCol();
        int row = mSouth.getMaxRow();

        // since all fields except one are already verified by mEast and mSouth
        // there is only the last field to check
        AtomToken atom = gameSituation.queryAtom(col, row, mEast
                .getColorIndex());
        if (atom == null)
            return null;

        // add all atoms to a new "square" molecule
        Molecule mBoth = new Molecule(mEast.getColorIndex());
        mBoth.addAtom(atom);
        mBoth.addAtoms(mEast.getAtoms());
        mBoth.addAtoms(mSouth.getAtoms());

        if (!mBoth.isValid())
            return null;

        return mBoth;

    }

    /**
     * Starts scanning at the given
     * <code>startAtom</atom> for a Molecule with 2*2 size.
     *
     * @param startAtom the Atom where to start the Molecule detection
     * @return the Molecule which includes the startAtom with the size of 2*2
     * Atoms
     */
    private Molecule detectSimpleMolecule(AtomToken startAtom) {
        Molecule molecule = null;

        int colorIndex = startAtom.getColorIndex();

        int row = startAtom.getField().getRow();
        int col = startAtom.getField().getCol();

        // search east for an atom
        AtomToken atomE = gameSituation.queryAtom(col + 1, row, colorIndex);
        if (atomE == null || isUsed(atomE))
            return null;

        // search south for an atom
        AtomToken atomS = gameSituation.queryAtom(col, row + 1, colorIndex);
        if (atomS == null || isUsed(atomS))
            return null;

        // search south east for an atom
        AtomToken atomSE = gameSituation
                .queryAtom(col + 1, row + 1, colorIndex);
        if (atomSE == null || isUsed(atomSE))
            return null;

        // add all atoms to a new "simple" molecule
        Molecule m = new Molecule(startAtom.getColorIndex());
        m.addAtom(startAtom);
        m.addAtom(atomE);
        m.addAtom(atomS);
        m.addAtom(atomSE);
        if (m.isValid()) {
            molecule = m;
        }

        return molecule;
    }

    /**
     * Checks whether the Atom is already in use by a Molecule or still
     * available by looping through all detected Molecules.
     *
     * @param atom the atom to check
     * @return true if not used, false if used by a molecule
     */
    private boolean isUsed(AtomToken atom) {
        boolean isUsed = false;
        for (Molecule m : detectedMolecules) {
            if (m.contains(atom)) {
                isUsed = true;
                break;
            }
        }

        return isUsed;
    }

}
