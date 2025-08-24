type Bludiste = [String]

otocVpravo :: Bludiste -> Bludiste
otocVpravo bludiste = [reverse [bludiste !! radek !! sloupec | radek <- [0..length bludiste - 1]] | sloupec <- [0..length (head bludiste) - 1]]
