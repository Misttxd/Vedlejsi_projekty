najdiDelitele :: Int -> [Int]
najdiDelitele 0 = []
najdiDelitele cislo = pomocnaFunkce 1
  where
    pomocnaFunkce aktualniDelitel
      | aktualniDelitel > cislo = []
      | cislo `mod` aktualniDelitel == 0 = aktualniDelitel : pomocnaFunkce (aktualniDelitel + 1)
      | otherwise = pomocnaFunkce (aktualniDelitel + 1)
