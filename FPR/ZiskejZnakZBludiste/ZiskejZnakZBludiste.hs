type Bludiste = [String]

ziskejZnakZBludiste :: Bludiste -> (Int, Int) -> Char
ziskejZnakZBludiste bludiste (indexRadku, indexSloupce) = (bludiste !! indexRadku) !! indexSloupce
