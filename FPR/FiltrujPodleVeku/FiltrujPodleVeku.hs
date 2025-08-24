filtrujPodleVeku :: [(String, Int)] -> Int -> [String]
filtrujPodleVeku seznamOsob maximalniVek = [jmeno | (jmeno, vek) <- seznamOsob, vek < maximalniVek]
