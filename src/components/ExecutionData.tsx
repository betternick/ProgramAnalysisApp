import { Flex, Heading, List, ListItem } from '@chakra-ui/react'
import React from 'react'
import { DynamicData } from '../types/MiscTypes'

type ExecutionDataProps = {
    data: DynamicData
}

export default function ExecutionData({ data }: ExecutionDataProps) {
    const { executionTimes, averageExecutionTime, averageMemoryUsage, averageCpuUsage, scaleFactor } = data
    const scaledColor = executionTimes > 0 ? `hsl(6,54%,${60 + Math.abs(scaleFactor) * 30}%)` : '#f5f5f5'
    console.log(scaledColor)
    return (
        <Flex
            flexDir="column"
            mt={2}
            borderRadius="10px"
            background={scaledColor}
            opacity={executionTimes === 0 ? 0.5 : 1}
            padding={2}
        >
            <Heading size="sm">Dynamic Data:</Heading>
            <List>
                <ListItem>Number of Times Executed: {executionTimes}</ListItem>
                <ListItem>Average Execution Time: {averageExecutionTime} ns</ListItem>
                <ListItem>Average Memory Usage: {Math.round(averageMemoryUsage / 1024)} kb</ListItem>
                <ListItem>Average CPU Usage: {(averageCpuUsage * 100).toFixed(2)}%</ListItem>
                <ListItem>{data.scaleFactor}</ListItem>
            </List>
        </Flex>
    )
}
