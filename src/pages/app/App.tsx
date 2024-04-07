import React, { useCallback, useEffect, useState } from 'react'
import Sidebar from '../../components/Sidebar'
import ControlFlowGraph from '../../components/ControlFlowGraph'
import { ResponseGraph } from '../../types/ControlFlowGraphTypes'
import { DynamicDataDeviationCalc } from '../../types/MiscTypes'
import { Flex } from '@chakra-ui/react'

// Written with help from ChatGPT
const determineDeviations = (nodes: DynamicDataDeviationCalc[]) => {
    const executionTimes: number[] = nodes.filter((n) => n.executionTimesDeviation.val !== 0).map((n) => n.executionTimesDeviation.val)
    const averageExecutionTime: number[] = nodes.filter((n) => n.averageExecutionTimeDeviation.val !== 0).map((n) => n.averageExecutionTimeDeviation.val)
    const averageMemoryUsage: number[] = nodes.filter((n) => n.averageMemoryUsageDeviation.val !== 0).map((n) => n.averageMemoryUsageDeviation.val)
    const averageCpuUsage: number[] = nodes.filter((n) => n.averageCpuUsageDeviation.val !== 0).map((n) => n.averageCpuUsageDeviation.val)

    const minExecutionTimes = Math.min(...executionTimes)
    const maxExecutionTimes = Math.max(...executionTimes)
    const minAverageExecutionTime = Math.min(...averageExecutionTime)
    const maxAverageExecutionTime = Math.max(...averageExecutionTime)
    const minAverageMemoryUsage = Math.min(...averageMemoryUsage)
    const maxAverageMemoryUsage = Math.max(...averageMemoryUsage)
    const minAverageCpuUsage = Math.min(...averageCpuUsage)
    const maxAverageCpuUsage = Math.max(...averageCpuUsage)

    nodes.forEach((n) => {
        n.executionTimesDeviation.deviation =
            maxExecutionTimes !== minExecutionTimes ? (n.executionTimesDeviation.val - minExecutionTimes) / (maxExecutionTimes - minExecutionTimes) : 0

        n.averageExecutionTimeDeviation.deviation =
            maxAverageExecutionTime !== minAverageExecutionTime
                ? (n.averageExecutionTimeDeviation.val - minAverageExecutionTime) / (maxAverageExecutionTime - minAverageExecutionTime)
                : 0
        n.averageMemoryUsageDeviation.deviation =
            maxAverageMemoryUsage !== minAverageMemoryUsage
                ? (n.averageMemoryUsageDeviation.val - minAverageMemoryUsage) / (maxAverageMemoryUsage - minAverageMemoryUsage)
                : 0
        n.averageCpuUsageDeviation.deviation =
            maxAverageCpuUsage !== minAverageCpuUsage ? (n.averageCpuUsageDeviation.val - minAverageCpuUsage) / (maxAverageCpuUsage - minAverageCpuUsage) : 0
    })
}

function App() {
    const [graph, setGraph] = useState<ResponseGraph[]>([])

    const handleUpload = (response: JSON) => {
        const newGraph = Object.keys(response).map((name) => ({ name, ...response[name] }))
        setGraph(newGraph)
    }

    const handleExecute = (response: JSON) => {
        if (response) {
            const deviationCalcs: DynamicDataDeviationCalc[] = []
            for (let id in response) {
                deviationCalcs.push({
                    id,
                    executionTimesDeviation: { val: response[id]['executionTimes'], deviation: 0 },
                    averageExecutionTimeDeviation: { val: response[id]['averageExecutionTime'], deviation: 0 },
                    averageMemoryUsageDeviation: { val: response[id]['averageMemoryUsage'], deviation: 0 },
                    averageCpuUsageDeviation: { val: response[id]['averageCpuUsage'], deviation: 0 },
                })
            }
            determineDeviations(deviationCalcs)
            const newGraph: ResponseGraph[] = graph.map((cfg) => {
                const newNodes = cfg.nodes.map((node) => {
                    if (node.id in response) {
                        const nodeDynamicDataCalc = deviationCalcs.find((n) => n.id === node.id)
                        node.dynamicData = { ...response[node.id], deviations: nodeDynamicDataCalc }
                    }
                    return { ...node }
                })
                return { ...cfg, nodes: newNodes }
            })
            setGraph(newGraph)
        }
    }

    const handleStatChange = useCallback((stat: string) => {
        setGraph((prev) =>
            prev.map((cfg) => ({
                ...cfg,
                nodes: cfg.nodes.map((n) => {
                    if (n.dynamicData) {
                        return { ...n, dynamicData: { ...n.dynamicData, stat } }
                    } else {
                        return n
                    }
                }),
            })),
        )
    }, [])

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
                    handleStatChange={handleStatChange}
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
