type Maze = [String]

printMaze :: Maze -> IO ()
printMaze x = putStr (concat (map (++"\n") x))

sample1 :: Maze
sample1 = ["*********",
           "* *   * *",
           "* * * * *",
           "* * * * *",
           "*   *   *",
           "******* *",
           "        *",
           "*********"]
sample2 :: Maze
sample2 = ["       ",
           "       ",
           "  ***  ",
           "  ***  ",
           "  ***  ",
           "       ",
           "       "]
sample3 :: Maze
sample3 = ["  * *  ",
           " ##### ",
           "  ***  ",
           "  * *  ",
           "  ***  ",
           "     * ",
           "       "]
sample4 :: Maze
sample4 = ["*********",
           "*s*   *e*",
           "* *   * *",
           "* *   * *",
           "*       *",
           "******* *",
           "        *",
           "*********"]
arrow :: Maze
arrow = [ "....#....",
          "...###...",
          "..#.#.#..",
          ".#..#..#.",
          "....#....",
          "....#....",
          "....#####"]

above :: Maze -> Maze -> Maze
above top bottom = top ++ bottom

-- Function to place two mazes side by side
beside :: Maze -> Maze -> Maze
beside left right = zipWith (++) left right
          
-- Funkcia na otočenie doprava
rotateR :: Maze -> Maze
rotateR maze = [reverse [maze !! row !! col | row <- [0..length maze - 1]] | col <- [0..length (head maze) - 1]]

-- Funkcia na otočenie doľava
rotateL :: Maze -> Maze
rotateL maze = [ [maze !! row !! col | row <- reverse [0..length maze - 1]] | col <- [0..length (head maze) - 1]]
