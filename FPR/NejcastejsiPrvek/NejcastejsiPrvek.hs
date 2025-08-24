import Data.List (group, sort, maximumBy)
import Data.Ord (comparing)

nejcastejsiPrvek :: [Int] -> Int
nejcastejsiPrvek seznamCisel = head $ maximumBy (comparing length) (group $ sort seznamCisel)
