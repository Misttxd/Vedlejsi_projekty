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
above x y = x ++ y              


sideBySide :: Maze -> Maze -> Maze
sideBySide xs ys = map (\(x,y) -> x ++ y)(zip xs ys)    
                                                       

sideBySide':: Maze -> Maze -> Maze      
sideBySide' (x:xs) (y:ys) = (x ++ y) : sideBySide' xs ys   
sideBySide' _ _ = []


toRow :: String -> Maze
toRow xs = [[x]|x<-xs]    


rotateR :: Maze -> Maze     
rotateR [x] = toRow x      
rotateR (x:xs) = (rotateR xs) `sideBySide` (toRow x)    
                                              


rotateL :: Maze -> Maze     
rotateL [x] = reverse(toRow x)  
rotateL (x:xs) = reverse(toRow x) `sideBySide` (rotateL xs)    

getFromMaze :: Maze -> (Int, Int) -> Char
getFromMaze maze (rowIndex, colIndex) = (maze !! rowIndex) !! colIndex     

putIntoMaze :: Maze -> [(Int, Int, Char)] -> Maze           
putIntoMaze maze changes = foldl updateMaze maze changes   

updateMaze :: Maze -> (Int, Int, Char) -> Maze     
updateMaze maze (rowIndex, colIndex, char) = take rowIndex maze                                 
                                                 ++ [replaceInRow (maze !! rowIndex) colIndex char] 
                                                 ++ drop (rowIndex + 1) maze                        

replaceInRow :: String -> Int -> Char -> String    
replaceInRow row colIndex char = take colIndex row ++ [char] ++ drop (colIndex + 1) row     

getPart :: Maze -> (Int,Int) -> (Int,Int) -> Maze  
getPart maze (startRow, startCol) (height, width) = map (take width . drop startCol) (take height . drop startRow $ maze)
