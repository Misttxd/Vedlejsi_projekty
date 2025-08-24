type Bludiste = [String]

vytiskniBludiste :: Bludiste -> IO ()
vytiskniBludiste bludiste = putStr (concat (map (++"\n") bludiste))

nadSebou :: Bludiste -> Bludiste -> Bludiste
nadSebou horniBludiste dolniBludiste = horniBludiste ++ dolniBludiste

vedleSebe :: Bludiste -> Bludiste -> Bludiste
vedleSebe leveBludiste praveBludiste = zipWith (++) leveBludiste praveBludiste

otocVpravo :: Bludiste -> Bludiste
otocVpravo bludiste = [reverse [bludiste !! radek !! sloupec | radek <- [0..length bludiste - 1]] | sloupec <- [0..length (head bludiste) - 1]]

otocVlevo :: Bludiste -> Bludiste
otocVlevo bludiste = [[bludiste !! radek !! sloupec | radek <- reverse [0..length bludiste - 1]] | sloupec <- [0..length (head bludiste) - 1]]

slouceniBludistVedleSebe :: Bludiste -> Bludiste -> Bludiste
slouceniBludistVedleSebe leveBludiste praveBludiste = map (\(radekLevy, radekPravy) -> radekLevy ++ radekPravy) (zip leveBludiste praveBludiste)

ziskejZnakZBludiste :: Bludiste -> (Int, Int) -> Char
ziskejZnakZBludiste bludiste (indexRadku, indexSloupce) = (bludiste !! indexRadku) !! indexSloupce

vlozDoBludiste :: Bludiste -> [(Int, Int, Char)] -> Bludiste
vlozDoBludiste puvodniBludiste seznamZmen = foldl aktualizujBludiste puvodniBludiste seznamZmen

akualizujBludiste :: Bludiste -> (Int, Int, Char) -> Bludiste
akualizujBludiste bludiste (indexRadku, indexSloupce, znak) =
  take indexRadku bludiste
  ++ [nahradVRadku (bludiste !! indexRadku) indexSloupce znak]
  ++ drop (indexRadku + 1) bludiste

nahradVRadku :: String -> Int -> Char -> String
nahradVRadku radek indexSloupce znak =
  take indexSloupce radek ++ [znak] ++ drop (indexSloupce + 1) radek

ziskejCastBludiste :: Bludiste -> (Int, Int) -> (Int, Int) -> Bludiste
ziskejCastBludiste puvodniBludiste (pocatecniRadek, pocatecniSloupec) (vyska, sirka) =
  map (take sirka . drop pocatecniSloupec) (take vyska . drop pocatecniRadek $ puvodniBludiste)
