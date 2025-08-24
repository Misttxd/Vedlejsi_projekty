type Bludiste = [String]

vytiskniBludiste :: Bludiste -> IO ()
vytiskniBludiste bludiste = putStr (concat (map (++"\n") bludiste))
