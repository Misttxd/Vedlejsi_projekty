type Bludiste = [String]

otocVlevo :: Bludiste -> Bludiste
otocVlevo bludiste = [[bludiste !! radek !! sloupec | radek <- reverse [0..length bludiste - 1]] | sloupec <- [0..length (head bludiste) - 1]]
