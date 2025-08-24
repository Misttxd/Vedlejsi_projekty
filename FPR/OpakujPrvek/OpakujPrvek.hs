opakujPrvek :: Int -> a -> [a]
opakujPrvek 0 _ = []
opakujPrvek pocetOpakovani prvekKOakovani = prvekKOakovani : opakujPrvek (pocetOpakovani - 1) prvekKOakovani
