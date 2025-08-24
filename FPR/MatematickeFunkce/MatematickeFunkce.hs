vypocitejMocninu :: Num a => a -> a
vypocitejMocninu cislo = cislo * cislo

vypocitejFaktorial :: Int -> Int
vypocitejFaktorial cislo
  | cislo == 0 = 1
  | otherwise  = cislo * vypocitejFaktorial (cislo - 1)

vypocitejFibonacci :: Int -> Int
vypocitejFibonacci poradi
  | poradi == 0 = 1
  | poradi == 1 = 1
  | otherwise   = vypocitejFibonacci (poradi - 2) + vypocitejFibonacci (poradi - 1)

jePrvocislo :: Int -> Bool
jePrvocislo 1 = False
jePrvocislo cislo = testPrvocisla cislo (cislo - 1)
  where
    testPrvocisla _ 1 = True
    testPrvocisla testovaneCislo delitel
      | testovaneCislo `mod` delitel == 0 = False
      | otherwise                         = testPrvocisla testovaneCislo (delitel - 1)

nejvetsiSpolecnyDelitel :: Int -> Int -> Int
nejvetsiSpolecnyDelitel cislo1 cislo2
  | cislo1 == cislo2 = cislo1
  | cislo1 > cislo2  = nejvetsiSpolecnyDelitel (cislo1 - cislo2) cislo2
  | cislo1 < cislo2  = nejvetsiSpolecnyDelitel cislo1 (cislo2 - cislo1)

jePrestupnyRok :: Int -> Bool
jePrestupnyRok rok = (rok `mod` 4 == 0 && rok `mod` 100 /= 0) || (rok `mod` 400 == 0)

najdiDelitele :: Int -> [Int]
najdiDelitele 0 = []
najdiDelitele cislo = pomocnaFunkce 1
  where
    pomocnaFunkce aktualniDelitel
      | aktualniDelitel > cislo = []
      | cislo `mod` aktualniDelitel == 0 = aktualniDelitel : pomocnaFunkce (aktualniDelitel + 1)
      | otherwise = pomocnaFunkce (aktualniDelitel + 1)

sectiKladnePrvky :: [Int] -> Int
sectiKladnePrvky [] = 0
sectiKladnePrvky (prvniPrvek:zbytekSeznamu)
  | prvniPrvek > 0 = prvniPrvek + sectiKladnePrvky zbytekSeznamu
  | otherwise      = sectiKladnePrvky zbytekSeznamu
