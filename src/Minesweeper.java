import java.util.*;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

/*- TEMPLATE
FIELDS:
... rows ...                            -- int
... cols ...                            -- int
... rand ...                            -- Random
... maxMines ...                       -- int
... minesLeft ...                      -- int
... nonminesLeft ...                   -- int
... cells ...                           -- ArrayList<ArrayList<ACell>>
METHODS:
... fillGrid() ...                      -- void
... makeNeighbors() ...                 -- void
... fillNumbers() ...                   -- void
... makeScene() ...                     -- WorldScene
 */

// Game class for initializing Minesweeper
class Minesweeper extends World {
  // represents the rows of cells
  int rows;
  int cols;
  Random rand;
  int maxMines;
  int minesLeft;
  int nonminesLeft;
  ArrayList<ArrayList<ACell>> cells;

  // Main constructor for Minesweeper game
  Minesweeper(int rows, int cols, int minesLeft) {
    this.rows = rows;
    this.cols = cols;
    this.rand = new Random();
    this.maxMines = this.rows * this.cols;
    this.minesLeft = minesLeft;
    this.nonminesLeft = this.rows * this.cols - this.minesLeft;
    this.cells = new ArrayList<ArrayList<ACell>>();
    this.fillGrid();
    this.makeNeighbors();
    this.fillNumbers();
    // Repoint neighbors to new Numbers
    this.makeNeighbors();
  }

  // Convenience constructor for tests
  Minesweeper(int rows, int cols, int minesLeft, ArrayList<ArrayList<ACell>> cells, Random rand) {
    this.rows = rows;
    this.cols = cols;
    this.rand = rand;
    this.maxMines = this.rows * this.cols;
    this.minesLeft = minesLeft;
    this.nonminesLeft = this.rows * this.cols - this.minesLeft;
    this.cells = cells;

    // Not calling game methods here so I can call each one individually for tests
    // using test_world which calls this constructor
  }

  // Fills the grid with empty cells and minesLeft mines in random cells
  void fillGrid() {
    int mine_slots = this.maxMines;
    int mines_not_filled = this.minesLeft;
    // fills the grid with Empty cells
    for (int i = 0; i < this.rows; i++) {
      this.cells.add(new ArrayList<>());
      for (int j = 0; j < this.cols; j++) {
        this.cells.get(i).add(new Empty());
      }
    }
    // Turns minesLeft cells into mines
    while (mines_not_filled > 0) {
      for (int i = 0; i < this.rows; i++) {
        for (int j = 0; j < this.cols; j++) {
          if (this.rand.nextInt(mine_slots) < mines_not_filled) {
            this.cells.get(i).set(j, new Mine());
            mines_not_filled--;
          }
          mine_slots--;
        }
      }
    }
  }

  // adds neighbors to each cell's neighbor arraylist
  void makeNeighbors() {
    ACell firstedge;
    ACell secondedge;
    ACell current;
    for (int i = 0; i < this.cols; i++) {
      firstedge = this.cells.get(0).get(i);
      secondedge = this.cells.get(this.rows - 1).get(i);
      if (i == 0) {
        // left corner cases
        firstedge.neighbors.add(this.cells.get(0).get(i + 1));
        firstedge.neighbors.add(this.cells.get(1).get(i + 1));
        firstedge.neighbors.add(this.cells.get(1).get(i));
        secondedge.neighbors.add(this.cells.get(this.rows - 1).get(i + 1));
        secondedge.neighbors.add(this.cells.get(this.rows - 2).get(i + 1));
        secondedge.neighbors.add(this.cells.get(this.rows - 2).get(i));
      }
      // right corner cases
      else if (i == this.cols - 1) {
        firstedge.neighbors.add(this.cells.get(0).get(this.cols - 2));
        firstedge.neighbors.add(this.cells.get(1).get(this.cols - 2));
        firstedge.neighbors.add(this.cells.get(1).get(this.cols - 1));
        secondedge.neighbors.add(this.cells.get(this.rows - 1).get(this.cols - 2));
        secondedge.neighbors.add(this.cells.get(this.rows - 2).get(this.cols - 2));
        secondedge.neighbors.add(this.cells.get(this.rows - 2).get(this.cols - 1));
      }
      else {
        // top and bottom edge cases
        firstedge.neighbors.add(this.cells.get(0).get(i + 1));
        firstedge.neighbors.add(this.cells.get(0).get(i - 1));
        firstedge.neighbors.add(this.cells.get(1).get(i));
        firstedge.neighbors.add(this.cells.get(1).get(i + 1));
        firstedge.neighbors.add(this.cells.get(1).get(i - 1));
        secondedge.neighbors.add(this.cells.get(this.rows - 1).get(i + 1));
        secondedge.neighbors.add(this.cells.get(this.rows - 1).get(i - 1));
        secondedge.neighbors.add(this.cells.get(this.rows - 2).get(i));
        secondedge.neighbors.add(this.cells.get(this.rows - 2).get(i + 1));
        secondedge.neighbors.add(this.cells.get(this.rows - 2).get(i - 1));
      }
    }
    for (int i = 1; i < this.rows - 1; i++) {
      // left and right edge cases
      firstedge = this.cells.get(i).get(0);
      secondedge = this.cells.get(i).get(this.cols - 1);
      firstedge.neighbors.add(this.cells.get(i + 1).get(0));
      firstedge.neighbors.add(this.cells.get(i - 1).get(0));
      firstedge.neighbors.add(this.cells.get(i + 1).get(1));
      firstedge.neighbors.add(this.cells.get(i).get(1));
      firstedge.neighbors.add(this.cells.get(i - 1).get(1));
      secondedge.neighbors.add(this.cells.get(i + 1).get(this.cols - 1));
      secondedge.neighbors.add(this.cells.get(i - 1).get(this.cols - 1));
      secondedge.neighbors.add(this.cells.get(i + 1).get(this.cols - 2));
      secondedge.neighbors.add(this.cells.get(i).get(this.cols - 2));
      secondedge.neighbors.add(this.cells.get(i - 1).get(this.cols - 2));
      // body cases
      for (int j = 1; j < this.cols - 1; j++) {
        current = this.cells.get(i).get(j);
        current.neighbors.add(this.cells.get(i).get(j - 1));
        current.neighbors.add(this.cells.get(i).get(j + 1));
        current.neighbors.add(this.cells.get(i - 1).get(j));
        current.neighbors.add(this.cells.get(i + 1).get(j));
        current.neighbors.add(this.cells.get(i - 1).get(j - 1));
        current.neighbors.add(this.cells.get(i - 1).get(j + 1));
        current.neighbors.add(this.cells.get(i + 1).get(j - 1));
        current.neighbors.add(this.cells.get(i + 1).get(j + 1));
      }
    }
  }

  // changes cells with neighboring mines to number cells
  void fillNumbers() {
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.cols; j++) {
        if (this.cells.get(i).get(j).isNumber()) {
          this.cells.get(i).set(j, new Number(this.cells.get(i).get(j).neighbors));
        }
      }
    }
  }

  // draws the game scene
  public WorldScene makeScene() {
    WorldScene scene = this.getEmptyScene();

    int posnx = 10;
    int posny = 10;
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.cols; j++) {
        scene.placeImageXY(this.cells.get(i).get(j).draw(), posnx, posny);
        posnx = posnx + 20;
      }
      posny = posny + 20;
      posnx = 10;
    }
    return scene;
  }

  // Handles revealing and flagging cells
  public void onMouseReleased(Posn pos, String buttonName) {
    int row_pos = pos.y / 20;
    int col_pos = pos.x / 20;

    if (buttonName.equals("LeftButton")) {
      this.cells.get(row_pos).get(col_pos).reveal();
      if (this.cells.get(row_pos).get(col_pos).endWorld()) {
        this.endOfWorld("Game Over");
      }
      if (this.won()) {
        this.endOfWorld("You Win!");
      }
    }
    else if (buttonName.equals("RightButton")) {
      if (this.cells.get(row_pos).get(col_pos).flagged) {
        this.cells.get(row_pos).get(col_pos).unflag();
      }
      else {
        this.cells.get(row_pos).get(col_pos).flag();
      }
    }
  }

  public boolean won() {
    for (ArrayList<ACell> row : this.cells) {
      for (ACell cell : row) {
        if (cell.notWon()) {
          return false;
        }
      }
    }
    return true;
  }

  public WorldScene lastScene(String msg) {
    for (ArrayList<ACell> row : this.cells) {
      for (ACell cell : row) {
        if (msg.equals("Game Over")) {
          cell.revealMine();
        }
        else {
          cell.revealFlag();
        }
      }
    }
    WorldScene lastscene = this.makeScene();
    lastscene.placeImageXY(new TextImage(msg, 20, FontStyle.BOLD, Color.black), this.cols * 10,
        this.rows * 10);
    return lastscene;
  }
}

/*- TEMPLATE
FIELDS:
... neighbors ...                       -- ArrayList<ACell>
... unclickedimg ...                    -- WorldImage
... hidden ...                          -- boolean
METHODS:
... draw() ...                          -- WorldImage
... countMines(int mines) ...           -- int
... addMine() ...                       -- int
... isNumber() ...                      -- boolean
 */

// Abstract class to represent each cell
abstract class ACell {
  ArrayList<ACell> neighbors;
  WorldImage unclickedimg;
  WorldImage flaggedimg;
  boolean hidden; // flag to handle revealed status
  boolean flagged; // flag to handle flagged status

  // Main Constructor for ACell
  ACell() {
    this.neighbors = new ArrayList<ACell>();
    this.unclickedimg = new FrameImage(new RectangleImage(20, 20, "solid", Color.lightGray));
    this.flaggedimg = new FrameImage(
        new OverlayImage(new TextImage("|", 13, FontStyle.BOLD, Color.black),
            new OverlayImage(new TextImage(" >", 13, FontStyle.BOLD, Color.red),
                new RectangleImage(20, 20, "solid", Color.lightGray))));
    this.hidden = true;
    this.flagged = false;
  }

  // Convenience constructor for tests
  ACell(boolean hidden) {
    this.neighbors = new ArrayList<ACell>();
    this.unclickedimg = new FrameImage(new RectangleImage(20, 20, "solid", Color.lightGray));
    this.hidden = hidden;
  }

  // Draws this cell onto the world scene
  abstract WorldImage draw();

  // counts neighboring mines of this cell
  int countMines(int mines) {
    for (ACell neighbor : this.neighbors) {
      mines += neighbor.addMine();
    }
    return mines;
  }

  // returns 0 as this is not a mine
  int addMine() {
    return 0;
  }

  // checks if this is a Number cell
  boolean isNumber() {
    return false;
  }

  // Reveals just this cell by turning hidden flag to false for number and mine
  // cells
  void reveal() {
    this.hidden = false;
  }

  // Flags this cell
  void flag() {
    this.flagged = true;
  }

  // Unflags this cell
  void unflag() {
    this.flagged = false;
  }

  // Checks if this is a mine to end the game
  boolean endWorld() {
    return false;
  }

  // Checks if this is not a mine and not revealed
  boolean notWon() {
    return this.hidden;
  }

  // Reveals all mines for when game is lost
  void revealMine() {
    // Does nothing for nonmine cells!
  }

  // Flags all mines for when game is won
  void revealFlag() {
    // Does nothing for nonmine cells!
  }
}

/*- TEMPLATE
FIELDS:
... neighbors ...                       -- ArrayList<ACell>
... unclickedimg ...                    -- WorldImage
... emptyimg ...                        -- WorldImage
... hidden ...                          -- boolean
METHODS:
... draw() ...                          -- WorldImage
... countMines(int mines) ...           -- int
... addMine() ...                       -- int
... isNumber() ...                      -- boolean
 */

// Class to represent an empty cell
class Empty extends ACell {
  WorldImage emptyimg;

  // Main Constructor
  Empty() {
    this.emptyimg = new FrameImage(new RectangleImage(20, 20, "solid", Color.gray));
  }

  // Convenience constructor for tests
  Empty(boolean hidden) {
    super(hidden);
    this.emptyimg = new FrameImage(new RectangleImage(20, 20, "solid", Color.gray));

  }

  // Draws this empty onto the world scene
  WorldImage draw() {
    if (this.hidden && !this.flagged) {
      return this.unclickedimg;
    }
    else if (this.hidden && this.flagged) {
      return this.flaggedimg;
    }
    else {
      return this.emptyimg;
    }
  }

  // checks if this is a Number cell
  boolean isNumber() {
    return this.countMines(0) > 0;
  }

  // Override for revealing this empty cell with potential flood-fill effect if
  // applicable
  void reveal() {
    this.hidden = false;

    if (this.countMines(0) == 0) {
      for (ACell n : this.neighbors) {
        if (n.hidden) {
          n.reveal();
        }
      }
    }
  }
}

/*- TEMPLATE
FIELDS:
... neighbors ...                       -- ArrayList<ACell>
... unclickedimg ...                    -- WorldImage
... numberimg ...                       -- WorldImage
... hidden ...                          -- boolean
... value ...                           -- int
METHODS:
... draw() ...                          -- WorldImage
... countMines(int mines) ...           -- int
... addMine() ...                       -- int
... isNumber() ...                      -- boolean
... getColor() ...                      -- Color
 */

// Class to represent a number cell
class Number extends ACell {
  int value;
  WorldImage numberimg;

  // Main Constructor
  Number(ArrayList<ACell> neighbors) {
    this.neighbors = neighbors;
    this.value = this.countMines(0);
    this.numberimg = new FrameImage(new OverlayImage(
        new TextImage(Integer.toString(this.value), 13, FontStyle.BOLD, this.getColor()),
        new RectangleImage(20, 20, "solid", Color.gray)));
  }

  // Convenience constructor for tests
  Number(ArrayList<ACell> neighbors, boolean hidden) {
    super(hidden);
    this.neighbors = neighbors;
    this.value = this.countMines(0);
    this.numberimg = new FrameImage(new OverlayImage(
        new TextImage(Integer.toString(this.value), 13, FontStyle.BOLD, this.getColor()),
        new RectangleImage(20, 20, "solid", Color.gray)));
  }

  // Draws this number onto the world scene
  WorldImage draw() {
    if (this.hidden && !this.flagged) {
      return this.unclickedimg;
    }
    else if (this.hidden && this.flagged) {
      return this.flaggedimg;
    }
    else {
      return this.numberimg;
    }
  }

  // Gets color of TextImage depending on value of number
  Color getColor() {
    if (this.value == 1) {
      return Color.blue;
    }
    else if (this.value == 2) {
      return Color.green;
    }
    else if (this.value == 3) {
      return Color.red;
    }
    else if (this.value == 4) {
      return Color.pink;
    }
    else if (this.value == 5) {
      return Color.magenta;
    }
    else if (this.value == 6) {
      return Color.cyan;
    }
    else if (this.value == 7) {
      return Color.black;
    }
    else {
      return Color.white;
    }
  }

}

/*- TEMPLATE
FIELDS:
... neighbors ...                       -- ArrayList<ACell>
... unclickedimg ...                    -- WorldImage
... mineimg ...                         -- WorldImage
... hidden ...                          -- boolean
METHODS:
... draw() ...                          -- WorldImage
... countMines(int mines) ...           -- int
... addMine() ...                       -- int
... isNumber() ...                      -- boolean
 */

// Class to represent a mine cell
class Mine extends ACell {
  WorldImage mineimg;

  // Main Constructor
  Mine() {
    this.mineimg = new FrameImage(new OverlayImage(new CircleImage(5, "solid", Color.red),
        new RectangleImage(20, 20, "solid", Color.gray)));
  }

  // Convenience constructor for tests
  Mine(boolean hidden) {
    this.hidden = hidden;
    this.mineimg = new FrameImage(new OverlayImage(new CircleImage(5, "solid", Color.red),
        new RectangleImage(20, 20, "solid", Color.gray)));
  }

  // Draws this mine onto the world scene
  WorldImage draw() {
    if (this.hidden && !this.flagged) {
      return this.unclickedimg;
    }
    else if (this.hidden && this.flagged) {
      return this.flaggedimg;
    }
    else {
      return this.mineimg;
    }
  }

  // returns 1 as this is a mine
  int addMine() {
    return 1;
  }

  // Checks if this is a mine to end the game
  boolean endWorld() {
    return true;
  }

  // Checks if this is not a mine and not revealed
  boolean notWon() {
    return false;
  }

  // Reveals all mines for when game is lost
  void revealMine() {
    this.hidden = false;
  }

  // Flags all mines for when game is won
  void revealFlag() {
    this.flagged = true;
  }

}

class Main {
  public static void main(String[] args) {
    Minesweeper world;
    System.out.print(args);
    if (args.length > 1) {
      try {
        world = new Minesweeper(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]));
      } catch (Exception e) {
        throw new IllegalArgumentException("Must include three integers for number of cells wide, number of cells high, and number of mines");
      }
    }
    else {
      world = new Minesweeper(15, 15, 30);
    }
    world.bigBang(world.cols * 20, world.rows * 20);
  }
}

// Examples and Tests
class ExamplesMinesweeper {
  ACell cell1;
  ACell cell2;
  ACell cell3;
  ACell cell4;
  ACell cell5;
  ACell cell6;
  ACell cell7;
  ACell cell8;
  ACell cell9;
  ACell cell10;
  ACell cell11;
  ArrayList<ACell> a1;
  ArrayList<ACell> a2;
  ArrayList<ArrayList<ACell>> aa1;
  ArrayList<ArrayList<ACell>> aa2;
  Minesweeper test_world;
  Minesweeper test_world2;
  Minesweeper test_world3;

  Minesweeper actual_world;
  Random rand = new Random(1);

  // resets examples to original values
  void reset() {
    cell1 = new Mine();
    cell2 = new Empty();
    cell3 = new Mine();
    cell4 = new Empty();
    cell5 = new Mine(false);
    cell6 = new Empty(false);
    cell7 = new Number(this.cell2.neighbors);
    cell8 = new Number(this.cell4.neighbors, false);
    cell9 = new Empty(true);
    cell10 = new Number(this.cell9.neighbors, true);
    cell11 = new Mine(true);

    a1 = new ArrayList<ACell>(Arrays.asList(this.cell1, this.cell2, this.cell3, this.cell4));
    a2 = new ArrayList<ACell>(Arrays.asList(this.cell9));
    aa1 = new ArrayList<ArrayList<ACell>>(Arrays.asList(this.a1));
    aa2 = new ArrayList<ArrayList<ACell>>(Arrays.asList(this.a2));
    test_world = new Minesweeper(15, 15, 30, this.aa1, this.rand);
    test_world2 = new Minesweeper(15, 15, 30, new ArrayList<ArrayList<ACell>>(), this.rand);
    test_world3 = new Minesweeper(1, 1, 0, this.aa2, this.rand);

    actual_world = new Minesweeper(15, 15, 30);

  }

  // Tests for Minesweeper

  void testMinesweeper(Tester t) {
    reset();
    this.actual_world.bigBang(this.actual_world.cols * 20, this.actual_world.rows * 20);
  }

  void testFillGrid(Tester t) {
    reset();
    t.checkExpect(this.aa1.size(), 1);
    t.checkExpect(this.aa1.get(0).size(), 4);
    this.test_world.fillGrid();
    t.checkExpect(this.aa1.size(), 16);
    t.checkExpect(this.aa1.get(1).size(), 15);

  }

  void testMakeNeighbors(Tester t) {
    reset();
    this.test_world2.fillGrid();
    // top left corner
    t.checkExpect(this.test_world2.cells.get(0).get(0).neighbors, new ArrayList<ACell>());
    // top right corner
    t.checkExpect(this.test_world2.cells.get(0).get(14).neighbors, new ArrayList<ACell>());
    // bottom left corner
    t.checkExpect(this.test_world2.cells.get(14).get(0).neighbors, new ArrayList<ACell>());
    // bottom right corner
    t.checkExpect(this.test_world2.cells.get(14).get(14).neighbors, new ArrayList<ACell>());
    // top edge
    t.checkExpect(this.test_world2.cells.get(0).get(1).neighbors, new ArrayList<ACell>());
    // left edge
    t.checkExpect(this.test_world2.cells.get(1).get(0).neighbors, new ArrayList<ACell>());
    // bottom edge
    t.checkExpect(this.test_world2.cells.get(14).get(1).neighbors, new ArrayList<ACell>());
    // right edge
    t.checkExpect(this.test_world2.cells.get(1).get(14).neighbors, new ArrayList<ACell>());
    // body
    t.checkExpect(this.test_world2.cells.get(1).get(1).neighbors, new ArrayList<ACell>());

    this.test_world2.makeNeighbors();

    // top left corner
    t.checkExpect(this.test_world2.cells.get(0).get(0).neighbors,
        new ArrayList<ACell>(Arrays.asList(this.test_world2.cells.get(0).get(1),
            this.test_world2.cells.get(1).get(1), this.test_world2.cells.get(1).get(0))));
    // top right corner
    t.checkExpect(this.test_world2.cells.get(0).get(14).neighbors,
        new ArrayList<ACell>(Arrays.asList(this.test_world2.cells.get(0).get(13),
            this.test_world2.cells.get(1).get(13), this.test_world2.cells.get(1).get(14))));
    // bottom left corner
    t.checkExpect(this.test_world2.cells.get(14).get(0).neighbors,
        new ArrayList<ACell>(Arrays.asList(this.test_world2.cells.get(14).get(1),
            this.test_world2.cells.get(13).get(1), this.test_world2.cells.get(13).get(0))));
    // bottom right corner
    t.checkExpect(this.test_world2.cells.get(14).get(14).neighbors,
        new ArrayList<ACell>(Arrays.asList(this.test_world2.cells.get(14).get(13),
            this.test_world2.cells.get(13).get(13), this.test_world2.cells.get(13).get(14))));
    // top edge
    t.checkExpect(this.test_world2.cells.get(0).get(1).neighbors,
        new ArrayList<ACell>(Arrays.asList(this.test_world2.cells.get(0).get(2),
            this.test_world2.cells.get(0).get(0), this.test_world2.cells.get(1).get(1),
            this.test_world2.cells.get(1).get(2), this.test_world2.cells.get(1).get(0))));
    // left edge
    t.checkExpect(this.test_world2.cells.get(1).get(0).neighbors,
        new ArrayList<ACell>(Arrays.asList(this.test_world2.cells.get(2).get(0),
            this.test_world2.cells.get(0).get(0), this.test_world2.cells.get(2).get(1),
            this.test_world2.cells.get(1).get(1), this.test_world2.cells.get(0).get(1))));
    // bottom edge
    t.checkExpect(this.test_world2.cells.get(14).get(1).neighbors,
        new ArrayList<ACell>(Arrays.asList(this.test_world2.cells.get(14).get(2),
            this.test_world2.cells.get(14).get(0), this.test_world2.cells.get(13).get(1),
            this.test_world2.cells.get(13).get(2), this.test_world2.cells.get(13).get(0))));
    // right edge
    t.checkExpect(this.test_world2.cells.get(1).get(14).neighbors,
        new ArrayList<ACell>(Arrays.asList(this.test_world2.cells.get(2).get(14),
            this.test_world2.cells.get(0).get(14), this.test_world2.cells.get(2).get(13),
            this.test_world2.cells.get(1).get(13), this.test_world2.cells.get(0).get(13))));
    // body
    t.checkExpect(this.test_world2.cells.get(1).get(1).neighbors,
        new ArrayList<ACell>(Arrays.asList(this.test_world2.cells.get(1).get(0),
            this.test_world2.cells.get(1).get(2), this.test_world2.cells.get(0).get(1),
            this.test_world2.cells.get(2).get(1), this.test_world2.cells.get(0).get(0),
            this.test_world2.cells.get(0).get(2), this.test_world2.cells.get(2).get(0),
            this.test_world2.cells.get(2).get(2))));

  }

  void testFillNumbers(Tester t) {
    reset();
    this.cell2.neighbors.add(this.cell1);
    this.test_world.fillGrid();
    this.test_world.makeNeighbors();
    t.checkExpect(this.test_world.cells.get(0).get(1), this.cell2);
    this.test_world.fillNumbers();
    t.checkExpect(this.test_world.cells.get(0).get(1), new Number(this.cell2.neighbors));

  }

  boolean testMakeScene(Tester t) {
    reset();
    WorldScene scene = new WorldScene(0, 0);
    scene.placeImageXY(this.cell9.draw(), 0, 0);
    return t.checkExpect(this.test_world3.makeScene(), scene);
  }

  void testOnMouseReleased(Tester t) {
    reset();
    this.test_world.fillGrid();

    t.checkExpect(this.test_world.cells.get(5).get(10).hidden, true);
    t.checkExpect(this.test_world.cells.get(1).get(1).flagged, false);

    this.test_world.onMouseReleased(new Posn(202, 105), "LeftButton");
    this.test_world.onMouseReleased(new Posn(25, 30), "RightButton");

    t.checkExpect(this.test_world.cells.get(5).get(10).hidden, false);
    t.checkExpect(this.test_world.cells.get(1).get(1).flagged, true);

    // Test for unflag
    this.test_world.onMouseReleased(new Posn(25, 30), "RightButton");

    t.checkExpect(this.test_world.cells.get(1).get(1).flagged, false);

  }

  void testWon(Tester t) {
    reset();
    this.test_world.fillGrid();
    t.checkExpect(this.test_world.won(), false);

    // revealing just a few nonmine cells
    this.test_world.onMouseReleased(new Posn(20, 0), "LeftButton");
    this.test_world.onMouseReleased(new Posn(80, 0), "LeftButton");

    t.checkExpect(this.test_world.won(), false);

    // revealing all nonmine cells to instigate win
    for (ArrayList<ACell> row : this.test_world.cells) {
      for (ACell cell : row) {
        cell.reveal();
      }
    }

    t.checkExpect(this.test_world.won(), true);

  }

  boolean testLastScene(Tester t) {
    reset();
    WorldScene gameover_scene = new WorldScene(0, 0);
    WorldScene win_scene = new WorldScene(0, 0);
    gameover_scene.placeImageXY(this.cell9.draw(), 0, 0);
    win_scene.placeImageXY(this.cell9.draw(), 0, 0);

    gameover_scene.placeImageXY(new TextImage("Game Over", 20, FontStyle.BOLD, Color.black), 10,
        10);
    win_scene.placeImageXY(new TextImage("You Win!", 20, FontStyle.BOLD, Color.black), 10, 10);

    this.test_world3.onMouseReleased(new Posn(0, 0), "LeftButton");
    return t.checkExpect(this.test_world3.lastScene("Game Over"), gameover_scene)
        && t.checkExpect(this.test_world3.lastScene("You Win!"), win_scene);
  }

  // Tests for ACell

  boolean testDraw(Tester t) {
    reset();
    return t.checkExpect(this.cell2.draw(),
        new FrameImage(new RectangleImage(20, 20, "solid", Color.lightGray)))
        && t.checkExpect(this.cell1.draw(),
            new FrameImage(new RectangleImage(20, 20, "solid", Color.lightGray)))
        && t.checkExpect(this.cell5.draw(),
            new FrameImage(new OverlayImage(new CircleImage(5, "solid", Color.red),
                new RectangleImage(20, 20, "solid", Color.gray))))
        && t.checkExpect(this.cell6.draw(),
            new FrameImage(new RectangleImage(20, 20, "solid", Color.gray)))
        && t.checkExpect(this.cell7.draw(),
            new FrameImage(new RectangleImage(20, 20, "solid", Color.lightGray)))
        && t.checkExpect(this.cell8.draw(),
            new FrameImage(new OverlayImage(
                new TextImage(Integer.toString(0), 13, FontStyle.BOLD, Color.white),
                new RectangleImage(20, 20, "solid", Color.gray))));
  }

  boolean testCountMines(Tester t) {
    reset();
    this.cell2.neighbors.add(this.cell1);
    this.cell2.neighbors.add(this.cell3);
    return t.checkExpect(this.cell2.countMines(0), 2) && t.checkExpect(this.cell4.countMines(0), 0);

  }

  boolean testAddMine(Tester t) {
    reset();
    return t.checkExpect(this.cell2.addMine(), 0) && t.checkExpect(this.cell1.addMine(), 1);
  }

  boolean testIsNumber(Tester t) {
    reset();
    this.cell4.neighbors.add(this.cell1);
    return t.checkExpect(this.cell1.isNumber(), false)
        && t.checkExpect(this.cell4.isNumber(), true);
  }

  boolean testReveal(Tester t) {
    reset();
    cell1.reveal();
    cell10.reveal();
    cell9.reveal();
    return t.checkExpect(cell1, new Mine(false))
        && t.checkExpect(cell10, new Number(this.cell9.neighbors, false))
        && t.checkExpect(cell9, new Empty(false));
  }

  boolean testFlag(Tester t) {
    reset();
    cell1.flag();
    cell2.flag();
    cell7.flag();
    return t.checkExpect(cell1.flagged, true) && t.checkExpect(cell2.flagged, true)
        && t.checkExpect(cell7.flagged, true);
  }

  boolean testEndWorld(Tester t) {
    reset();
    return t.checkExpect(cell1.endWorld(), true) && t.checkExpect(cell2.endWorld(), false)
        && t.checkExpect(cell7.endWorld(), false);
  }

  boolean testNotWon(Tester t) {
    reset();
    cell9.hidden = false;
    return t.checkExpect(cell1.notWon(), false) && t.checkExpect(cell2.notWon(), true)
        && t.checkExpect(cell7.notWon(), true) && t.checkExpect(cell9.notWon(), false);
  }

  boolean testRevealMine(Tester t) {
    reset();
    cell9.revealMine();
    cell10.revealMine();
    cell11.revealMine();
    return t.checkExpect(cell9, new Empty(true))
        && t.checkExpect(cell10, new Number(this.cell9.neighbors, true))
        && t.checkExpect(cell11, cell5);
  }

  boolean testRevealFlag(Tester t) {
    reset();
    cell9.revealFlag();
    cell10.revealFlag();
    cell11.revealFlag();
    return t.checkExpect(cell9, new Empty(true))
        && t.checkExpect(cell10, new Number(this.cell9.neighbors, true))
        && t.checkExpect(cell11.flagged, true);
  }
}
