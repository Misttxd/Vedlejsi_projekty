import Data.Char
import Data.Function (on)
import Distribution.Simple.Build (repl)
import Data.List (group, sort, maximumBy, groupBy, sortOn, nub)
import Data.Ord (comparing)

change :: [a] -> [(Int, Int)] -> [a]
change xs [] = xs
change xs ((start, len):ops) = change (reverseSegment xs start len) ops
  where
    reverseSegment list s l = take s list ++ reverse (take l (drop s list)) ++ drop (s + l) list

--5

filter' :: [(String, Int)] -> Int -> [String]
filter' xs n = [name |(name, age) <- xs, age < n] 

positions :: String -> Char -> [Int]
positions str ch = [i | (c, i) <- zip str [0..], c == ch]


--6

average :: [(String, Int)] -> Double
average xs = fromIntegral(sum ages) / fromIntegral (length ages)
            where
                ages = map snd xs

changes :: Eq a => [(a,b)] -> a -> b -> [(a,b)]
changes xs key newValue = map (\(k,v) -> if key == k then (k, newValue) else (k,v)) xs

buildText :: [(String, Int)] -> String
buildText xs = concatMap (\(text, n) -> concat (replicate n text)) xs
