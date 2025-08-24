import Data.Char
import Distribution.Simple.Build (repl)
import Data.List (group, sort, maximumBy, groupBy, sortOn, nub)
import Data.Ord (comparing)
import Data.Function (on)

oddList :: Int -> Int -> [Int]
oddList 0 0 = []
oddList x y | x <= y && x `mod` 2 == 1 = x : oddList (x+2) y
            | x `mod` 2 == 0 = oddList (x+1) y
            |otherwise = oddList 0 0

mostFrequent :: [Int] -> Int
mostFrequent xs = head $ maximumBy (comparing length) (group $ sort xs)

oddList2 :: Int -> Int -> [Int]
oddList2 x y = [x | x <- [x..y], x `mod` 2 == 1]

getHashMap :: (a -> Int) -> [a] -> [(Int, [a])]
getHashMap hash xs = map (\g -> (hash (head g), g)) grouped
  where
    sorted = sortOn hash xs
    grouped = groupBy ((==) `on` hash) sorted

removeAllUpper :: String -> String
removeAllUpper [] = []
removeAllUpper xs = [x | x <- xs, not (isUpper x)]

filterRemoveAllUpper :: String -> String
filterRemoveAllUpper txt = filter isLower txt

union :: Eq a => [a] -> [a] -> [a]
union xs ys = xs ++ [y | y <- ys, not (elem y xs)]

filterUnion :: Eq a => [a] -> [a] -> [a]
filterUnion xs ys = xs ++ filter (\x -> not (elem x xs)) ys