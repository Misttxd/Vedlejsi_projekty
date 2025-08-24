obratSeznam :: [a] -> [a]
obratSeznam [] = []
obratSeznam (prvniPrvek:zbytekSeznamu) = obratSeznam zbytekSeznamu ++ [prvniPrvek]
