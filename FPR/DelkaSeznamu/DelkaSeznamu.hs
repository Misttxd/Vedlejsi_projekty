delkaSeznamu :: [a] -> Int
delkaSeznamu [] = 0
delkaSeznamu (_:zbytekSeznamu) = 1 + delkaSeznamu zbytekSeznamu
