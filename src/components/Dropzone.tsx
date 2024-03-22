import {Box, Flex, Text} from '@chakra-ui/react'
import React, {useCallback} from 'react'
import {useDropzone} from 'react-dropzone'

type DropzoneProps = {
    dropHandler: (files: Array<File>) => void
    text?: string
}

const Dropzone = ({dropHandler, text}: DropzoneProps) => {
    const onDrop = useCallback(dropHandler, [dropHandler])
    const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

    return (
        <Box
            p={6}
            bg="gray.50"
            borderColor="gray.200"
            borderWidth={1}
            borderRadius="lg"
            borderStyle="dashed"
            _hover={{borderColor: 'gray.400'}}
            {...getRootProps()}
        >
            <input {...getInputProps({webkitdirectory: 'true'})} />
            {isDragActive ? (
                <p>Drop your program files ...</p>
            ) : (
                <Flex
                    flexDirection="column"
                    justifyContent="center"
                    alignItems="center"
                >
                    <Text>{text ? text : 'Drag & drop your program files here'}</Text>
                    <Text fontStyle="italic">(Note: Only *.java files are accepted)</Text>
                </Flex>
            )}
        </Box>
    )
}

export default Dropzone
