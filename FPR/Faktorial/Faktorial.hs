vypocitejFaktorial :: Int -> Int
vypocitejFaktorial cislo
  | cislo == 0 = 1
  | otherwise  = cislo * vypocitejFaktorial (cislo - 1)
