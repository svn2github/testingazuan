function FCKeditor_OnComplete( editorInstance ) {
        completeName = editorInstance.Name;
        instanceName = completeName.substring(13, completeName.length);
        var functocall = 'initializeNotes' + instanceName +'()';
        eval(functocall);
}