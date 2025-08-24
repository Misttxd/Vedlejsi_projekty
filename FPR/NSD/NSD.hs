nejvetsiSpolecnyDelitel :: Int -> Int -> Int
nejvetsiSpolecnyDelitel cislo1 cislo2
  | cislo1 == cislo2 = cislo1
  | cislo1 > cislo2  = nejvetsiSpolecnyDelitel (cislo1 - cislo2) cislo2
  | cislo1 < cislo2  = nejvetsiSpolecnyDelitel cislo1 (cislo2 - cislo1)
