jePrvocislo :: Int -> Bool
jePrvocislo 1 = False
jePrvocislo cislo = testPrvocisla cislo (cislo - 1)
  where
    testPrvocisla _ 1 = True
    testPrvocisla testovaneCislo delitel
      | testovaneCislo `mod` delitel == 0 = False
      | otherwise                         = testPrvocisla testovaneCislo (delitel - 1)
