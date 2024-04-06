import React, { useState } from 'react'
import Sidebar from '../../components/Sidebar'
import { Flex } from '@chakra-ui/react'
import ControlFlowGraph from '../../components/ControlFlowGraph'
import { ResponseGraph } from '../../types/ControlFlowGraphTypes'

function App() {
    const [graph, setGraph] = useState<ResponseGraph[]>([])

    const handleUpload = (response: JSON) => {
        const newGraph = Object.keys(response).map((name) => ({ name, ...response[name] }))
        setGraph(newGraph)
    }

    const handleExecute = (response: JSON) => {
        if (response) {
            const newGraph: ResponseGraph[] = graph.map((cfg) => {
                const newNodes = cfg.nodes.map((node) => {
                    if (node.id in response) {
                        node.dynamicData = response[node.id]
                    }
                    return { ...node }
                })
                return { ...cfg, nodes: newNodes }
            })
            setGraph(newGraph)
        }
    }

    return (
        <Flex
            width="100vw"
            height="100vh"
        >
            <title>Program Analyser</title>
            <Flex
                width="30vw"
                height="100vh"
                justifyContent="center"
                flexDirection="column"
                boxShadow="rgba(0, 0, 0, 0.15) 1.95px 1.95px 2.6px"
            >
                <Sidebar
                    handleExecute={handleExecute}
                    handleUpload={handleUpload}
                    graph={graph}
                />
            </Flex>
            <Flex
                width="70vw"
                justifyContent="center"
            >
                <ControlFlowGraph graph={graph} />
            </Flex>
        </Flex>
    )
}

export default App
