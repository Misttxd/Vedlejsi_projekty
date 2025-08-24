import Data.Char (toUpper)

prevedNaVelkaPismena :: String -> String
prevedNaVelkaPismena [] = []
prevedNaVelkaPismena (prvniZnak:zbytekRetezce) = toUpper prvniZnak : prevedNaVelkaPismena zbytekRetezce
