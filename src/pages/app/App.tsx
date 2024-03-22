import React from 'react'
import UploadFiles from '../../components/UploadFiles'
import {Container} from '@chakra-ui/react'

function App() {
    return (
        <Container minW="100vw" centerContent>
            <UploadFiles />
        </Container>
    )
}

export default App
