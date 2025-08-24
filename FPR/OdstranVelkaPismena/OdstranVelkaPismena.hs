import Data.Char (isUpper)

odstranVelkaPismena :: String -> String
odstranVelkaPismena [] = []
odstranVelkaPismena (znak:zbytekRetezce)
  | isUpper znak = odstranVelkaPismena zbytekRetezce
  | otherwise    = znak : odstranVelkaPismena zbytekRetezce
