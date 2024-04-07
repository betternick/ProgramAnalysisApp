import React, { useState } from 'react'
import Sidebar from '../../components/Sidebar'
import { Flex } from '@chakra-ui/react'
import ControlFlowGraph from '../../components/ControlFlowGraph'
import { ResponseGraph } from '../../types/ControlFlowGraphTypes'
import { DynamicDataCalc } from '../../types/MiscTypes'

// Written with help from ChatGPT
const determineDeviations = (nodes: DynamicDataCalc[]) => {
    const filteredNodes = nodes.filter((n) => n.val !== 0)
    const average = filteredNodes.reduce((acc, n) => acc + n.val, 0) / filteredNodes.length
    nodes.forEach((n) => (n.deviation = n.val - average))
}

function App() {
    const [graph, setGraph] = useState<ResponseGraph[]>([])

    const handleUpload = (response: JSON) => {
        const newGraph = Object.keys(response).map((name) => ({ name, ...response[name] }))
        setGraph(newGraph)
    }

    const handleExecute = (response: JSON) => {
        if (response) {
            const memoryValues: DynamicDataCalc[] = []
            for (let id in response) {
                if ('averageMemoryUsage' in response[id]) {
                    memoryValues.push({ id, val: response[id]['averageMemoryUsage'], deviation: 0, scaleFactor: 0 })
                }
            }
            // Written with help from ChatGPT
            determineDeviations(memoryValues)
            const maxDeviation = Math.max(...memoryValues.map((n) => n.deviation))
            const scaleFactor = maxDeviation !== 0 ? 1 / maxDeviation : 0
            memoryValues.forEach((n) => (n.scaleFactor = n.deviation * scaleFactor))

            const newGraph: ResponseGraph[] = graph.map((cfg) => {
                const newNodes = cfg.nodes.map((node) => {
                    if (node.id in response) {
                        const nodeScaleFactor = memoryValues.find((mv) => mv.id === node.id)?.scaleFactor
                        node.dynamicData = { ...response[node.id], scaleFactor: nodeScaleFactor }
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
