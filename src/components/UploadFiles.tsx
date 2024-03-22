import {useDropzone} from 'react-dropzone'
import React, {useCallback, useState} from 'react'
import {FaRegFileCode} from 'react-icons/fa'
import {Icon, Button, Box, Flex, SimpleGrid, Spinner, Heading, Text} from '@chakra-ui/react'

declare module 'react' {
    interface InputHTMLAttributes<T> extends HTMLAttributes<T> {
        webkitdirectory?: string
    }
}

const UploadFiles = () => {
    const [fileNames, setFileNames] = useState<string[]>([])
    const [files, setFiles] = useState<File[]>([])
    const [isUploading, setIsUploading] = useState<boolean>(false)

    const onDrop = useCallback(
        (acceptedFiles: Array<File>) => {
            const javaFiles: File[] = acceptedFiles.filter((file) => file.name.endsWith('.java') && !fileNames.includes(file.name))
            setFileNames((prev) => [...prev, ...javaFiles.map((file) => file.name)])
            setFiles((prev) => [...prev, ...javaFiles])
        },
        [fileNames],
    )

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

    const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})
    return (
        <Flex minW="50vw" mt={2} borderWidth={1} borderRadius="lg" p={6} direction="column" gap={2}>
            <Heading size="md">Step 1: Upload your program files</Heading>
            <Box>
                {fileNames.length > 0 ? (
                    <Box>
                        <Text fontWeight="bold">Selected Files: </Text>
                        <SimpleGrid columns={2} spacing={1} m={2}>
                            {fileNames.map((name, key) => (
                                <Box key={key}>
                                    <Icon as={FaRegFileCode} mr={1} />
                                    {name}
                                </Box>
                            ))}
                        </SimpleGrid>
                        <Box p={6} bg="#fafafa" borderWidth={1} borderRadius="lg" borderStyle="dashed">
                            <div {...getRootProps()}>
                                <input {...getInputProps({webkitdirectory: 'true'})} />
                                {isDragActive ? <p>Drop more files here ...</p> : <p>Add more files</p>}
                            </div>
                        </Box>
                    </Box>
                ) : (
                    <Box p={6} bg="#fafafa" borderWidth={1} borderRadius="lg" borderStyle="dashed">
                        <Box {...getRootProps()}>
                            <input {...getInputProps({webkitdirectory: 'true'})} />
                            {isDragActive ? (
                                <Text>Drop your program files here ...</Text>
                            ) : (
                                <Flex flexDirection="column" justifyContent="center" alignItems="center">
                                    <Text>Drag & drop your program files here, or click to select</Text>
                                    <Text fontStyle="italic">(Note: Only *.java files will be accepted)</Text>
                                </Flex>
                            )}
                        </Box>
                    </Box>
                )}
            </Box>
            <Button onClick={handleSubmit} colorScheme="teal">
                Submit
            </Button>
            {isUploading && (
                <Box display="flex" justifyContent="center">
                    <Spinner />
                </Box>
            )}
        </Flex>
    )
}

export default UploadFiles
