type Bludiste = [String]

vlozDoBludiste :: Bludiste -> [(Int, Int, Char)] -> Bludiste
vlozDoBludiste puvodniBludiste seznamZmen = foldl aktualizujBludiste puvodniBludiste seznamZmen

aktualizujBludiste :: Bludiste -> (Int, Int, Char) -> Bludiste
aktualizujBludiste bludiste (indexRadku, indexSloupce, znak) =
  take indexRadku bludiste
  ++ [nahradVRadku (bludiste !! indexRadku) indexSloupce znak]
  ++ drop (indexRadku + 1) bludiste

nahradVRadku :: String -> Int -> Char -> String
nahradVRadku radek indexSloupce znak =
  take indexSloupce radek ++ [znak] ++ drop (indexSloupce + 1) radek
