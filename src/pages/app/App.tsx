import React from 'react'
import Uploader from '../../components/Uploader'
import {Container} from '@chakra-ui/react'

function App() {
    return (
        <Container
            minW="100vw"
            centerContent
        >
            <Uploader />
        </Container>
    )
}

export default App
