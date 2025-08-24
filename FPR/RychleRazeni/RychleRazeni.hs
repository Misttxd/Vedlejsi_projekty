rychleRazeni :: (Ord a) => [a] -> [a]
rychleRazeni [] = []
rychleRazeni (pivot:zbytekSeznamu) =
  rychleRazeni [prvek | prvek <- zbytekSeznamu, prvek <= pivot]
  ++ [pivot]
  ++ rychleRazeni [prvek | prvek <- zbytekSeznamu, prvek > pivot]
