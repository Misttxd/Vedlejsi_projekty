sestavTextOpakovanim :: [(String, Int)] -> String
sestavTextOpakovanim seznamOpakovani = concatMap (\(castTextu, pocetOpakovani) -> concat (replicate pocetOpakovani castTextu)) seznamOpakovani
