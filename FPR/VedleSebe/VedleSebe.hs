type Bludiste = [String]

vedleSebe :: Bludiste -> Bludiste -> Bludiste
vedleSebe leveBludiste praveBludiste = zipWith (++) leveBludiste praveBludiste
