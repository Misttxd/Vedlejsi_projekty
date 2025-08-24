data Article
    = TextBlock String -- Represents a block of text
    | Section String [Article] -- Represents a section with a title and a list of subsections or text blocks
    deriving (Show)

-- Example Article Structure
exampleArticle :: Article
exampleArticle = Section "Introduction"
    [ TextBlock "This is the introduction."
    , Section "Subsection 1"
        [ TextBlock "Details about subsection 1."
        ]
    , Section "Subsection 2"
        [ TextBlock "Details about subsection 2."
        ]
    ]

-- Definition of the Component data type
data Component
    = TextBox { name :: String, text :: String }
    | Button { name :: String, value :: String }
    | Container { name :: String, children :: [Component] }
    deriving (Show)

-- Function to list all names
listAllNames :: Component -> [String]
listAllNames (TextBox name _) = [name]
listAllNames (Button name _) = [name]
listAllNames (Container name children) = name : listNamesFromChildren children
  where
    -- Helper function to process children explicitly
    listNamesFromChildren :: [Component] -> [String]
    listNamesFromChildren [] = []
    listNamesFromChildren (x:xs) = listAllNames x ++ listNamesFromChildren xs

-- Example GUI structure
gui :: Component
gui = Container "My App"
        [ Container "Menu"
            [ Button "btn_new" "New"
            , Button "btn_open" "Open"
            , Button "btn_close" "Close"
            ]
        , Container "Body"
            [ TextBox "textbox_1" "Some text goes here" ]
        , Container "Footer" []
        ]



removeElements :: Component -> [String] -> Component
removeElements (TextBox name text) namesToRemove
    | name `elem` namesToRemove = Container "" [] -- Replace with an empty Container if name matches
    | otherwise = TextBox name text
removeElements (Button name value) namesToRemove
    | name `elem` namesToRemove = Container "" [] -- Replace with an empty Container if name matches
    | otherwise = Button name value
removeElements (Container name children) namesToRemove
    | name `elem` namesToRemove = Container name [] -- Keep the container but clear its children if name matches
    | otherwise = Container name (removeElementsFromChildren children namesToRemove)
  where
    removeElementsFromChildren :: [Component] -> [String] -> [Component]
    removeElementsFromChildren [] _ = []
    removeElementsFromChildren (x:xs) names = removeElements x names : removeElementsFromChildren xs names
