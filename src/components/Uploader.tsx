import React, {useState} from 'react'
import {IoIosRemove} from 'react-icons/io'
import {Tooltip, Button, Box, Flex, SimpleGrid, Spinner, Heading, Text, IconButton} from '@chakra-ui/react'
import Dropzone from './Dropzone'

declare module 'react' {
    interface InputHTMLAttributes<T> extends HTMLAttributes<T> {
        webkitdirectory?: string
    }
}

const Uploader = () => {
    const [fileNames, setFileNames] = useState<string[]>([])
    const [files, setFiles] = useState<File[]>([])
    const [isUploading, setIsUploading] = useState<boolean>(false)

    const handleDrop = (droppedFiles: Array<File>) => {
        const javaFiles: File[] = droppedFiles.filter((file) => file.name.endsWith('.java') && !fileNames.includes(file.name))
        setFileNames((prev) => [...prev, ...javaFiles.map((file) => file.name)])
        setFiles((prev) => [...prev, ...javaFiles])
    }

    const handleRemove = (fileName: string) => {
        setFileNames((prev) => prev.filter((file) => file !== fileName))
        setFiles((prev) => prev.filter((file) => file.name !== fileName))
    }

    const handleSubmit = async () => {
        if (files.length === 0) return
        const fd = new FormData()
        files.forEach((file, idx) => {
            fd.append(`file_${idx}`, file)
        })
        const url = 'http://httpbin.org/post'
        setIsUploading(true)
        const results = await fetch(url, {
            method: 'POST',
            body: fd,
        })
            .then((res) => {
                if (res.ok) {
                    setFiles([])
                    setFileNames([])
                    setIsUploading(false)
                    return res.json()
                }
            })
            .catch((err) => {
                console.log(err)
            })
        console.log(results)
    }

    return (
        <Flex
            minW="60vw"
            mt={2}
            borderWidth={1}
            borderRadius="lg"
            p={6}
            direction="column"
            gap={2}
        >
            <Heading size="md">Step 1: Upload your program files</Heading>
            <Box>
                {fileNames.length > 0 ? (
                    <Box>
                        <Text fontWeight="bold">Selected Files: </Text>
                        <SimpleGrid
                            columns={2}
                            spacing={1}
                            m={2}
                        >
                            {fileNames.map((name, key) => (
                                <Box key={key}>
                                    <Tooltip
                                        aria-label="Remove file"
                                        label="Remove file"
                                        placement="auto"
                                    >
                                        <IconButton
                                            aria-label="Remove file"
                                            icon={<IoIosRemove />}
                                            isRound={true}
                                            variant="ghost"
                                            size="xs"
                                            mr={1}
                                            onClick={() => handleRemove(name)}
                                        />
                                    </Tooltip>
                                    {name}
                                </Box>
                            ))}
                        </SimpleGrid>
                        <Box>
                            <Dropzone
                                dropHandler={handleDrop}
                                text={'Upload additional files'}
                            />
                        </Box>
                    </Box>
                ) : (
                    <Box>
                        <Dropzone dropHandler={handleDrop} />
                    </Box>
                )}
            </Box>
            <Button
                onClick={handleSubmit}
                colorScheme="teal"
            >
                Submit
            </Button>
            {isUploading && (
                <Box
                    display="flex"
                    justifyContent="center"
                >
                    <Spinner />
                </Box>
            )}
        </Flex>
    )
}

export default Uploader
