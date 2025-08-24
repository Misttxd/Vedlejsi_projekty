import Data.Char (toUpper, isUpper)

prevedNaVelkaPismena :: String -> String
prevedNaVelkaPismena [] = []
prevedNaVelkaPismena (prvniZnak:zbytekRetezce) = toUpper prvniZnak : prevedNaVelkaPismena zbytekRetezce

odstranVelkaPismena :: String -> String
odstranVelkaPismena [] = []
odstranVelkaPismena (znak:zbytekRetezce)
  | isUpper znak = odstranVelkaPismena zbytekRetezce
  | otherwise    = znak : odstranVelkaPismena zbytekRetezce

sestavTextOpakovanim :: [(String, Int)] -> String
sestavTextOpakovanim seznamOpakovani = concatMap (\(castTextu, pocetOpakovani) -> concat (replicate pocetOpakovani castTextu)) seznamOpakovani
