vypocitejFibonacci :: Int -> Int
vypocitejFibonacci poradi
  | poradi == 0 = 1
  | poradi == 1 = 1
  | otherwise   = vypocitejFibonacci (poradi - 2) + vypocitejFibonacci (poradi - 1)
