import React, { useState } from 'react'
import Uploader from '../../components/Uploader'
import { Flex } from '@chakra-ui/react'
import ControlFlowGraph from '../../components/ControlFlowGraph'
import { ResponseGraph } from '../../types/ControlFlowGraphTypes'

function App() {
    const [graph, setGraph] = useState<null | JSON>(null)

    const controlFlowGraph = (): ResponseGraph[] => {
        if (!graph) return []
        return Object.keys(graph).map((name): ResponseGraph => ({ name, ...graph[name] }))
    }

    return (
        <Flex
            width="100vw"
            height="100vh"
        >
            <title>Control Flow Graph Generator</title>
            <Flex
                width="30vw"
                height="100vh"
                justifyContent="center"
                flexDirection="column"
                boxShadow="rgba(0, 0, 0, 0.15) 1.95px 1.95px 2.6px"
            >
                <Uploader handleResponse={setGraph} />
            </Flex>
            <Flex
                width="70vw"
                justifyContent="center"
            >
                <ControlFlowGraph graph={controlFlowGraph()} />
            </Flex>
        </Flex>
    )
}

export default App
