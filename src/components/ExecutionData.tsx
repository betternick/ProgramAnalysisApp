import { Flex, Heading, List, ListItem } from '@chakra-ui/react'
import React from 'react'
import { DynamicData } from '../types/MiscTypes'

type ExecutionDataProps = {
    data: DynamicData
}

export default function ExecutionData({ data }: ExecutionDataProps) {
    // const executionTimes = data ? data.executionTimes.toString() : 'N/A'
    // const averageExecutionTime = data ? data.averageExecutionTime.toString() : 'N/A'
    // const averageMemoryUsage = data ? data.averageMemoryUsage.toString() : 'N/A'
    // const averageCpuUsage = data ? data.averageCpuUsage.toString() : 'N/A'
    const { executionTimes, averageExecutionTime, averageMemoryUsage, averageCpuUsage } = data

    return (
        <Flex
            flexDir="column"
            mt={2}
            borderRadius="10px"
            background="#f5f5f5"
            padding={2}
        >
            <Heading size="sm">Dynamic Data:</Heading>
            <List>
                <ListItem>Execution Times: {executionTimes}</ListItem>
                <ListItem>Average Execution Time: {averageExecutionTime}</ListItem>
                <ListItem>Average Memory Usage: {averageMemoryUsage}</ListItem>
                <ListItem>Average CPU Usage: {averageCpuUsage}</ListItem>
            </List>
        </Flex>
    )
}
