package com.geolud.atomica.objects;

import java.util.ArrayList;

/**
 * This class represents molecules made up by atoms. Atoms have to be arranged
 * as a rectangle on the game board not containing empty fields in the middle.
 * Only atoms with the same color can build a valid molecule object.
 *
 * @author Georg Ludewig
 */
public class Molecule {
    /**
     * The atoms the molecule is made up of.
     */
    private ArrayList<AtomToken> atoms = null;

    /**
     * The minimum row the molecule has atoms in.
     */
    private int minRow = -1;

    /**
     * The maximum row the molecule has atoms in.
     */
    private int maxRow = -1;

    /**
     * The minimum column the molecule has atoms in.
     */
    private int minCol = -1;

    /**
     * The maximum column the molecule has atoms in.
     */
    private int maxCol = -1;

    /**
     * The color of the molecule. All atoms have to be in that color.
     */
    private int colorIndex;

    /**
     * Constructs an empty molecule with the given color without atoms.
     *
     * @param colorIndex the color of the molecule
     */
    public Molecule(int colorIndex) {
        this.colorIndex = colorIndex;
        this.atoms = new ArrayList<AtomToken>();
    }

    /**
     * Constructs a new Molecule identical to the given one.
     *
     * @param m the molecule to clone
     */
    public Molecule(Molecule m) {
        this(m.getColorIndex());
        this.atoms = new ArrayList<AtomToken>(m.atoms);
        this.minCol = m.minCol;
        this.minRow = m.minRow;
        this.maxCol = m.maxCol;
        this.maxRow = m.maxRow;
    }

    /**
     * Adds an atom to the molecule if it's not already part of the molecule and
     * has the same color like the molecule.
     *
     * @param atom the atom to add
     */
    public void addAtom(AtomToken atom) {
        if (atoms.contains(atom))
            return;

        Field field = atom.getField();

        int row = field.getRow();
        int col = field.getCol();

        if (atom.getColorIndex() != colorIndex) {
            return;
        }

        if (row < minRow || minRow == -1)
            minRow = row;
        if (col < minCol || minCol == -1)
            minCol = col;
        if (row > maxRow || maxRow == -1)
            maxRow = row;
        if (col > maxCol || maxCol == -1)
            maxCol = col;

        this.atoms.add(atom);
    }

    /**
     * Adds all the given atoms to the molecule.
     *
     * @param atoms the atoms to add
     */
    public void addAtoms(ArrayList<AtomToken> atoms) {
        for (AtomToken atom : atoms) {
            addAtom(atom);
        }
    }

    /**
     * Checks if the given atom is part of the molecule.
     *
     * @param atom the atom to be checked
     * @return true if the atom is part of the molecule
     */
    public boolean contains(AtomToken atom) {
        return atoms.contains(atom);

    }

    /**
     * Returns the atom at the specific position.
     *
     * @param i the index of the atom to return
     * @return the atom at the specific position
     */
    public AtomToken getAtom(int i) {
        return atoms.get(i);
    }

    public ArrayList<AtomToken> getAtoms() {
        return atoms;
    }

    /**
     * Returns the color index of the molecule.
     *
     * @return the color index of the molecule
     */
    public int getColorIndex() {
        return colorIndex;
    }

    /**
     * Returns the number of columns the molecule takes place on the board.
     *
     * @return the number of columns the molecule takes place on the board
     */
    public int getCols() {
        return (maxCol - minCol) + 1;
    }

    /**
     * Returns the maximum column the atoms take place.
     *
     * @return the maximum column the atoms take place
     */
    public int getMaxCol() {
        return maxCol;
    }

    /**
     * Returns the maximum row the atoms take place.
     *
     * @return the maximum row the atoms take place
     */
    public int getMaxRow() {
        return maxRow;
    }

    /**
     * Returns the minimum column the atoms take place.
     *
     * @return the minimum column the atoms take place
     */
    public int getMinCol() {
        return minCol;
    }

    /**
     * Returns the minimum row the atoms take place.
     *
     * @return the minimum row the atoms take place
     */
    public int getMinRow() {
        return minRow;
    }

    /**
     * Returns the number of rows the molecule is involved in.
     *
     * @return the number of rows the molecule is involved in
     */
    public int getRows() {
        return (maxRow - minRow) + 1;
    }

    /**
     * Checks if a molecule is made up of valid atoms arrangement.
     *
     * @return true if the molecule is valid
     */
    public boolean isValid() {
        int rows = getRows();
        int cols = getCols();
        return atoms.size() >= 4 && atoms.size() == (rows * cols);

    }

    /**
     * The number of atoms the molecule is made up of.
     *
     * @return number of atoms the molecule is made up of
     */
    public int size() {
        return atoms.size();
    }
}
