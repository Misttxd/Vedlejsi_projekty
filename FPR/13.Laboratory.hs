-- -- Definition of Attribute
-- data Attribute = Attribute
--     { name  :: String
--     , value :: String
--     } deriving (Show)

-- -- Definition of Tag
-- data Tag = Tag
--     { tagName    :: String
--     , attributes :: [Attribute]
--     , children   :: [Tag]
--     } deriving (Show)

-- -- Definition of HTMLDocument
-- data HTMLDocument = HTMLDocument
--     { tags :: [Tag]
--     } deriving (Show)

-- -- Example HTML document
-- exampleHTML :: HTMLDocument
-- exampleHTML = HTMLDocument
--     [ Tag "html" [] 
--         [ Tag "head" []
--             [ Tag "title" [] []
--             ]
--         , Tag "body" [Attribute "class" "main"]
--             [ Tag "h1" [Attribute "id" "header"] []
--             , Tag "p" [] []
--             ]
--         ]
--     ]


-- Definition of Component
data Component
    = TextBox { name :: String, text :: String }
    | Button { name :: String, value :: String }
    | Container { name :: String, children :: [Component] }
    deriving (Show)

-- Function to print the path to a component by name
printPath :: Component -> String -> String
printPath (TextBox cname _) targetName
    | cname == targetName = cname
    | otherwise = ""
printPath (Button cname _) targetName
    | cname == targetName = cname
    | otherwise = ""
printPath (Container cname children) targetName =
    findPath children cname
  where
    -- Helper function to recursively find the path in children
    findPath :: [Component] -> String -> String
    findPath [] _ = ""
    findPath (x:xs) parentName =
        let path = printPath x targetName
        in if path /= ""
           then parentName ++ " / " ++ path
           else findPath xs parentName

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


-- Function to remove a component from a container at a specific index
removeComponentFromContainerAtIndex :: Component -> String -> Int -> Component
removeComponentFromContainerAtIndex (Container cname children) targetName index
    | cname == targetName = Container cname (removeAtIndex children index)
    | otherwise = Container cname (removeFromChildren children)
  where
    -- Helper function to remove an element at a specific index
    removeAtIndex :: [Component] -> Int -> [Component]
    removeAtIndex [] _ = []
    removeAtIndex (x:xs) 0 = xs
    removeAtIndex (x:xs) n = x : removeAtIndex xs (n - 1)

    -- Helper function to recursively remove from children
    removeFromChildren :: [Component] -> [Component]
    removeFromChildren [] = []
    removeFromChildren (x:xs) =
        removeComponentFromContainerAtIndex x targetName index : removeFromChildren xs
removeComponentFromContainerAtIndex component _ _ = component -- Do nothing for non-container components
