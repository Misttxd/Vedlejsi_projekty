sjednoceniSeznamu :: Eq a => [a] -> [a] -> [a]
sjednoceniSeznamu prvniSeznam druhySeznam = prvniSeznam ++ [prvek | prvek <- druhySeznam, not (elem prvek prvniSeznam)]
